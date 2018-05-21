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
import com.basicsteps.multipos.model.entities.UnitEntity
import com.basicsteps.multipos.utils.JsonUtils
import com.basicsteps.multipos.worker.handling.dao.UnitEntityDao
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

class TaxHandler(vertx: Vertx) : BaseCRUDHandler(vertx) {


    fun getTaxList(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            findAll(message, dbManager.taxDao!!)
        }
    }

    fun createTax(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            save(message, dbManager.taxDao!!)
        }
    }

    fun createTaxList(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            saveAll(message, dbManager.taxDao!!)
        }
    }

    fun deleteTax(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            trash(message, dbManager.taxDao!!)
        }
    }

    fun updateTax(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            update(message, dbManager.taxDao!!)
        }
    }

    fun getTaxById(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            findById(message, dbManager.taxDao!!)
        }
    }

}