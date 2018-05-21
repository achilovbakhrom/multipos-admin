package com.basicsteps.multipos.worker.handling.handler.config

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

class CurrencyHandler(vertx: Vertx) : BaseCRUDHandler(vertx) {

    fun findAll(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            findAll(message, dbManager.currencyDao!!)
        }

    }

    fun findById(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            findById(message, dbManager.currencyDao!!)
        }

    }

    fun update(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            update(message, dbManager.currencyDao!!)
        }
    }
}