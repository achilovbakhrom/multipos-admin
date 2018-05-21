package com.basicsteps.multipos.worker.handling.handler.config

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

class DiscountHandler(vertx: Vertx) : BaseCRUDHandler(vertx) {

    fun getDiscountList(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            findAll(message, dbManager.discountDao!!)
        }
    }

    fun createDiscount(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            save(message, dbManager.discountDao!!)
        }
    }

    fun createDiscountList(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            saveAll(message, dbManager.discountDao!!)
        }
    }

    fun deleteDiscount(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            trash(message, dbManager.discountDao!!)
        }
    }

    fun updateDiscount(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            update(message, dbManager.discountDao!!)
        }
    }

    fun getDiscountById(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            findById(message, dbManager.discountDao!!)
        }
    }

}