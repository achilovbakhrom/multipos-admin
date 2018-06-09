package com.basicsteps.multipos.worker.handling.handler.config

import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import com.basicsteps.multipos.core.model.exceptions.NotExistsException
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


class InventoryHandler(vertx: Vertx) : BaseCRUDHandler(vertx) {


//    fun getEmployeeList(message: Message<String>) {
//        if (message.body() != null) {
//            val jsonObject = JsonObject(message.body())
//            val tenantId = jsonObject.getString("tenantId")
//            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
//
//            findAll(message, dbManager.inventoryDao!!)
//        }
//    }
//
//    fun createEmployee(message: Message<String>) {
//        if (message.body() != null) {
//            val jsonObject = JsonObject(message.body())
//            val tenantId = jsonObject.getString("tenantId")
//            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
//
//            save(message, dbManager.inventoryDao!!)
//        }
//    }
//
//    fun deleteEmployee(message: Message<String>) {
//        if (message.body() != null) {
//            val jsonObject = JsonObject(message.body())
//            val tenantId = jsonObject.getString("tenantId")
//            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
//
//            trash(message, dbManager.inventoryDao!!)
//        }
//    }
//
//    fun updateEmployee(message: Message<String>) {
//        if (message.body() != null) {
//            val jsonObject = JsonObject(message.body())
//            val tenantId = jsonObject.getString("tenantId")
//            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
//            update(message, dbManager.inventoryDao!!)
//        }
//    }
//
//    fun getEmployeeById(message: Message<String>) {
//        if (message.body() != null) {
//            val jsonObject = JsonObject(message.body())
//            val tenantId = jsonObject.getString("tenantId")
//            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
//            findById(message, dbManager.inventoryDao!!)
//        }
//    }

    fun surplusProduct(message: Message<String>) {

        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            val surplusProduct = JsonUtils.toPojo<SurplusProduct>(json = jsonObject.getJsonObject("data").toString())
            surplusProduct.userId = jsonObject.getString("userId")
            var ref = mutableListOf<IncomingProduct>()
            dbManager
                    .surplusProductDao
                    ?.save(surplusProduct)
                    ?.map({
                        it.listOfProducts
                    })
                    ?.flatMap ({
                        ref = it.toMutableList()
                        val productStateList = mutableListOf<ProductState>()
                        for (item in it){
                            val element = ProductState()
                            element.productId = item.productId
                            element.warehouseId = surplusProduct.warehouseId
                            element.purchased = item.purchased
                            element.quantity = item.quantity
                            element.unitId = item.unitId
                            element.vendorId = surplusProduct.vendorId

                            productStateList.add(element)
                        }
//                        dbManager.productDao?.saveAll(it, invoice.userId!!)
                        dbManager.productStateDao?.saveAllIfNotExists(productStateList)
                    })
                    ?.flatMap({
                        val inventoryList = mutableListOf<Inventory>()
                        for (item in ref){
                            val element = Inventory()
                            element.unitId = item.unitId
                            element.quantity = item.quantity
                            element.productId = item.productId
                            element.vendorId = surplusProduct.vendorId
                            element.sourceId = surplusProduct.id
                            element.operation = InventoryOperation.SURPLUS.value()
                            inventoryList.add(element)
                        }
                        dbManager.inventoryDao?.saveAll(inventoryList, surplusProduct.userId!!)
                    })
                    ?.flatMap({
                        val productCostList = mutableListOf<ProductCost>()
                        for (item in ref){
                            val element = ProductCost()
                            element.vendorId = surplusProduct.vendorId
                            element.currencyId = surplusProduct.currencyId
                            element.cost = item.cost
                            element.productId = item.productId
                            element.date = surplusProduct.createdTime
                            productCostList.add(element)
                        }
                        dbManager.productCostDao?.saveAll(productCostList, surplusProduct.userId!!)
                    })
                    ?.flatMap({
                        val warehouseQueue = mutableListOf<WarehouseQueue>()
                        for (item in ref){
                            val element = WarehouseQueue()
                            element.productionDate = item.productionDate
                            element.expiryDate = item.expiryDate
                            element.operation = InventoryOperation.SURPLUS.value()
                            element.sourceId = surplusProduct.id
                            element.quantitySold = 0.0
                            element.quantityReceived = item.quantity
                            element.quantityAvailable = item.quantity
                            element.warehouseId = surplusProduct.warehouseId
                            element.incomingProductId = item.productId
                            warehouseQueue.add(element)
                        }
                        dbManager.warehouseQueueDao?.saveAll(warehouseQueue, surplusProduct.userId!!)
                    })
                    ?.subscribe({
                        message.reply(MultiPosResponse(surplusProduct, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    }, {
                        when (it) {
                            is DataStoreException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                            is WriteDbFailedException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                        }
                    })

//            save(message, dbManager.invoiceDao!!)
        }
    }

    fun wasteProduct(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)


            val wasteProduct = JsonUtils.toPojo<WasteProduct>(json = jsonObject.getJsonObject("data").toString())
            wasteProduct.userId = jsonObject.getString("userId")
            var ref = mutableListOf<ListItem>()
            dbManager
                    .wasteProductDao
                    ?.save(wasteProduct)
                    ?.map({
                        it.listOfProducts
                    })
                    ?.flatMap ({
                        ref = it.toMutableList()
                        dbManager.productStateDao?.decreaseProductStateCountList(it)
                    })
                    ?.flatMap({
                        dbManager.inventoryDao?.addOperation(ref, orderId = wasteProduct.id!!, operationType = InventoryOperation.WASTE.value())
                    })
                    ?.flatMap({
                        dbManager.warehouseQueueDao?.decreaseWarehouseQueueList(ref)
                    })
                    ?.subscribe({
                        message.reply(MultiPosResponse(wasteProduct, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    }, {
                        when (it) {
                            is DataStoreException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                            is WriteDbFailedException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                            is NotExistsException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                        }
                    })

//            save(message, dbManager.orderDao!!)
        }
    }

    fun returnFromCustomer(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            val returnFromCustomer = JsonUtils.toPojo<ReturnFromCustomer>(json = jsonObject.getJsonObject("data").toString())
            returnFromCustomer.userId = jsonObject.getString("userId")
            var ref = mutableListOf<IncomingProduct>()
            dbManager
                    .returnFromCustomerDao
                    ?.save(returnFromCustomer)
                    ?.map({
                        it.listOfProducts
                    })
                    ?.flatMap ({
                        ref = it.toMutableList()
                        val productStateList = mutableListOf<ProductState>()
                        for (item in it){
                            val element = ProductState()
                            element.productId = item.productId
                            element.warehouseId = returnFromCustomer.warehouseId
                            element.purchased = item.purchased
                            element.quantity = item.quantity
                            element.unitId = item.unitId
                            element.vendorId = returnFromCustomer.vendorId

                            productStateList.add(element)
                        }
//                        dbManager.productDao?.saveAll(it, invoice.userId!!)
                        dbManager.productStateDao?.saveAllIfNotExists(productStateList)
                    })
                    ?.flatMap({
                        val inventoryList = mutableListOf<Inventory>()
                        for (item in ref){
                            val element = Inventory()
                            element.unitId = item.unitId
                            element.quantity = item.quantity
                            element.productId = item.productId
                            element.vendorId = returnFromCustomer.vendorId
                            element.sourceId = returnFromCustomer.id
                            element.operation = InventoryOperation.RETURN_OF_SALE.value()
                            inventoryList.add(element)
                        }
                        dbManager.inventoryDao?.saveAll(inventoryList, returnFromCustomer.userId!!)
                    })
                    ?.flatMap({
                        val productCostList = mutableListOf<ProductCost>()
                        for (item in ref){
                            val element = ProductCost()
                            element.vendorId = returnFromCustomer.vendorId
                            element.currencyId = returnFromCustomer.currencyId
                            element.cost = item.cost
                            element.productId = item.productId
                            element.date = returnFromCustomer.createdTime
                            productCostList.add(element)
                        }
                        dbManager.productCostDao?.saveAll(productCostList, returnFromCustomer.userId!!)
                    })
                    ?.flatMap({
                        val warehouseQueue = mutableListOf<WarehouseQueue>()
                        for (item in ref){
                            val element = WarehouseQueue()
                            element.productionDate = item.productionDate
                            element.expiryDate = item.expiryDate
                            element.operation = InventoryOperation.RETURN_OF_SALE.value()
                            element.sourceId = returnFromCustomer.id
                            element.quantitySold = 0.0
                            element.quantityReceived = item.quantity
                            element.quantityAvailable = item.quantity
                            element.warehouseId = returnFromCustomer.warehouseId
                            element.incomingProductId = item.productId
                            warehouseQueue.add(element)
                        }
                        dbManager.warehouseQueueDao?.saveAll(warehouseQueue, returnFromCustomer.userId!!)
                    })
                    ?.flatMap({
                        dbManager.paymentDao?.saveAll(returnFromCustomer.listOfPayments!!, returnFromCustomer.userId!!)
                    })
                    ?.subscribe({
                        message.reply(MultiPosResponse(returnFromCustomer, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    }, {
                        when (it) {
                            is DataStoreException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                            is WriteDbFailedException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                        }
                    })

//            save(message, dbManager.invoiceDao!!)
        }
    }

    fun returnToVendor(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            val returnToVendor = JsonUtils.toPojo<ReturnToVendor>(json = jsonObject.getJsonObject("data").toString())
            returnToVendor.userId = jsonObject.getString("userId")
            var ref = mutableListOf<ListItem>()
            dbManager
                    .returnToVendorDao
                    ?.save(returnToVendor)
                    ?.map({
                        it.listOfProducts
                    })
                    ?.flatMap ({
                        ref = it.toMutableList()
                        dbManager.productStateDao?.decreaseProductStateCountList(it)
                    })
                    ?.flatMap({
                        dbManager.inventoryDao?.addOperation(ref, orderId = returnToVendor.id!!, operationType = InventoryOperation.RETURN_TO_VENDOR.value())
                    })
                    ?.flatMap({
                        dbManager.warehouseQueueDao?.decreaseWarehouseQueueList(ref)
                    })
                    ?. flatMap({
                        dbManager.paymentDao?.saveAll(returnToVendor.listOfPayments!!, returnToVendor.userId!!)
                    })
                    ?.subscribe({
                        message.reply(MultiPosResponse(returnToVendor, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    }, {
                        when (it) {
                            is DataStoreException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                            is WriteDbFailedException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                            is NotExistsException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                        }
                    })

//            save(message, dbManager.orderDao!!)
        }
    }

}