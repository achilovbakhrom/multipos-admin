package com.basicsteps.multipos.worker.handling.handler.config

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

class PaymentTypeHandler(vertx: Vertx) : BaseCRUDHandler(vertx) {

    fun getPaymentTypeList(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            findAll(message, dbManager.paymentTypeDao!!)
        }
    }

    fun createPaymentType(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            save(message, dbManager.paymentTypeDao!!)
        }
    }

    fun updatePaymentType(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            update(message, dbManager.paymentTypeDao!!)
        }
    }

    fun getPaymentTypeById(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            findById(message, dbManager.paymentTypeDao!!)
        }
    }

    fun deletePaymentType(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            trash(message, dbManager.paymentTypeDao!!)
        }
    }

}