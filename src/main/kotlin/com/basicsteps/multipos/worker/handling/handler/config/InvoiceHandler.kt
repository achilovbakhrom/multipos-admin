package com.basicsteps.multipos.worker.handling.handler.config


import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.model.exceptions.WriteDbFailedException
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.model.InventoryOperation
import com.basicsteps.multipos.model.StatusMessages
import com.basicsteps.multipos.model.entities.*
import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

class InvoiceHandler(vertx: Vertx) : BaseCRUDHandler(vertx) {

    //TODO handle logic, CHECK CREATE, do others (no delete)

    fun getInvoiceList(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            findAll(message, dbManager.invoiceDao!!)
        }
    }

    fun createInvoice(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            val invoice = JsonUtils.toPojo<Invoice>(json = jsonObject.getJsonObject("data").toString())
            invoice.userId = jsonObject.getString("userId")
            var ref = mutableListOf<IncomingProduct>()
            dbManager
                    .invoiceDao
                    ?.save(invoice)
                    ?.map({
                        it.listOfProducts
                    })
                    ?.flatMap ({
                        ref = it.toMutableList()
                        val productStateList = mutableListOf<ProductState>()
                        for (item in it){
                            val element = ProductState()
                            element.productId = item.productId
                            element.warehouseId = invoice.warehouseId
                            element.purchased = item.purchased
                            element.quantity = item.quantity
                            element.unitId = item.unitId
                            element.vendorId = invoice.vendorId

                            productStateList.add(element)
                        }
//                        dbManager.productDao?.saveAll(it, invoice.userId!!)
                        dbManager.productStateDao?.saveAll(productStateList, invoice.userId!!)
                    })
                    ?.flatMap({
                        val inventoryList = mutableListOf<Inventory>()
                        for (item in it){
                            val element = Inventory()
                            element.unitId = item.unitId
                            element.quantity = item.quantity
                            element.productId = item.productId
                            element.vendorId = item.vendorId
                            element.sourceId = invoice.id
                            element.operation = InventoryOperation.INVOICE.value()
                            inventoryList.add(element)
                        }
                        dbManager.inventoryDao?.saveAll(inventoryList, invoice.userId!!)
                    })
                    ?.flatMap({
                        val productCostList = mutableListOf<ProductCost>()
                        for (item in ref){
                            val element = ProductCost()
                            element.vendorId = invoice.vendorId
                            element.currencyId = invoice.currencyId
                            element.cost = item.cost
                            element.productId = item.productId
                            element.date = invoice.createdTime
                            productCostList.add(element)
                        }
                        dbManager.productCostDao?.saveAll(productCostList, invoice.userId!!)
                    })
                    ?.flatMap({
                        dbManager.paymentDao?.saveAll(invoice.listOfPayments!!, invoice.userId!!)
                    })
                    ?.subscribe({
                        message.reply(MultiPosResponse(invoice, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    }, {
                        when (it) {
                            is DataStoreException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                            is WriteDbFailedException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                        }
                    })

//            save(message, dbManager.invoiceDao!!)
        }
    }

    fun deleteInvoice(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            trash(message, dbManager.invoiceDao!!)
        }
    }

    fun updateInvoice(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            update(message, dbManager.invoiceDao!!)
        }
    }

    fun getInvoiceById(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            findById(message, dbManager.invoiceDao!!)
        }
    }

}