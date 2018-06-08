package com.basicsteps.multipos.worker.handling.handler.config

import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import com.basicsteps.multipos.core.model.exceptions.NotExistsException
import com.basicsteps.multipos.core.model.exceptions.WriteDbFailedException
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.model.StatusMessages
import com.basicsteps.multipos.model.entities.Inventory
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

    fun changeProductCountInventory(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            val inventory = JsonUtils.toPojo<Inventory>(json = jsonObject.getJsonObject("data").toString())
            inventory.userId = jsonObject.getString("userId")

            dbManager
                    .inventoryDao
                    ?.save(inventory)
                    ?.flatMap ({
                        dbManager.productStateDao?.changeProductStateCount(it)
                    })
                    ?.subscribe({
                        message.reply(MultiPosResponse(inventory, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    }, {
                        when (it) {
                            is DataStoreException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                            is WriteDbFailedException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                            is NotExistsException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                        }
                    })
        }
    }

}