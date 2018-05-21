package com.basicsteps.multipos.worker.handling.handler.config

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

class ProductHandler(vertx: Vertx) : BaseCRUDHandler(vertx) {

    fun getProductList(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            findAll(message, dao = dbManager.productDao!!)
        }
    }

    fun createProduct(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            save(message, dao = dbManager.productDao!!)
        }
    }

    fun updateProduct(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            update(message, dao = dbManager.productDao!!)
        }
    }

    fun getProductById(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            findById(message, dao = dbManager.productDao!!)
        }
    }

    fun trashProductById(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            trashById(message, dao = dbManager.productDao!!)
        }
    }

}