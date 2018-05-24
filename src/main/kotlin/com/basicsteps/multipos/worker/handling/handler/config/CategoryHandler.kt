package com.basicsteps.multipos.worker.handling.handler.config

import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.model.entities.Category
import com.basicsteps.multipos.worker.handling.dao.CategoryDao
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

class CategoryHandler(vertx: Vertx) : BaseCRUDHandler(vertx){
    fun getCategoryList(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            findAll(message, dao = dbManager.categoryDao!!)
        }
    }

    fun createCategory(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            save(message, dao = dbManager.categoryDao!!)
        }
    }

    fun updateCategory(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            update(message, dao = dbManager.categoryDao!!)
        }
    }

    fun getCategoryById(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            findById(message, dao = dbManager.categoryDao!!)
        }
    }

    fun trashCategoryById(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            trashById(message, dao = dbManager.categoryDao!!)
        }
    }
}