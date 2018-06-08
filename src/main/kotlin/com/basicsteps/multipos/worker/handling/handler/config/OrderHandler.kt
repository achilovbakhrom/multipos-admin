package com.basicsteps.multipos.worker.handling.handler.config

import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import com.basicsteps.multipos.core.dao.DataStoreException
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

class OrderHandler(vertx: Vertx) : BaseCRUDHandler(vertx) {


    fun getOrderList(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            findAll(message, dbManager.orderDao!!)
        }
    }

    fun createOrder(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)


            val order = JsonUtils.toPojo<Order>(json = jsonObject.getJsonObject("data").toString())
            order.userId = jsonObject.getString("userId")
            var ref = mutableListOf<ListItem>()
            dbManager
                    .orderDao
                    ?.save(order)
                    ?.map({
                        it.listOfProducts
                    })
                    ?.flatMap ({
                        ref = it.toMutableList()
                        dbManager.productStateDao?.decreaseProductStateCountList(it)
                    })
                    ?.flatMap({
                        dbManager.inventoryDao?.addSaleOperation(ref, orderId = order.id!!)
                    })
                    ?.flatMap({
                        dbManager.warehouseQueueDao?.decreaseWarehouseQueueList(ref)
                    })
                    ?. flatMap({
                        dbManager.paymentDao?.saveAll(order.listOfPayments!!, order.userId!!)
                    })
                    ?.subscribe({
                        message.reply(MultiPosResponse(order, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
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

    fun deleteOrder(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            trash(message, dbManager.orderDao!!)
        }
    }

    fun updateOrder(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            update(message, dbManager.orderDao!!)
        }
    }

    fun getOrderById(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            findById(message, dbManager.orderDao!!)
        }
    }

}