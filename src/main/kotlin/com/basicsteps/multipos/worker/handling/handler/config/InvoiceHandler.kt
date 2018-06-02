package com.basicsteps.multipos.worker.handling.handler.config


import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.model.exceptions.NotExistsException
import com.basicsteps.multipos.core.model.exceptions.ReadDbFailedException
import com.basicsteps.multipos.core.model.exceptions.UpdateDbFailedException
import com.basicsteps.multipos.core.model.exceptions.WriteDbFailedException
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.core.response.MultiposRequest
import com.basicsteps.multipos.model.StatusMessages
import com.basicsteps.multipos.model.entities.*
import com.basicsteps.multipos.utils.JsonUtils
import com.basicsteps.multipos.worker.handling.dao.UnitEntityDao
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

            dbManager
                    .invoiceDao
                    ?.save(invoice)
                    ?.map({
                        it.listOfProducts
                    })
                    ?.flatMap ({
//                        dbManager.productDao?.saveAll(it, invoice.userId!!)
                        dbManager.incomingProductDao?.saveAll(it, invoice.userId!!)
                    })
                    ?.flatMap({
                        val warehouseQueueList = mutableListOf<WarehouseQueue>()
                        for (item in it){
                            val element = WarehouseQueue()
                            element.incomingProductId = item.productId
                            element.warehouseId = invoice.warehouseId
                            element.quantityAvailable = item.quantity
                            element.quantityReceived = item.quantity
                            element.quantitySold = 0.0
                            warehouseQueueList.add(element)
                        }
                        dbManager.warehouseQueueDao?.saveAll(warehouseQueueList, invoice.userId!!)
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