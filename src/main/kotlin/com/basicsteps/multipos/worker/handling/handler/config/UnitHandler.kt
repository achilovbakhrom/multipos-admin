package com.basicsteps.multipos.worker.handling.handler.config

import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import com.basicsteps.multipos.core.model.RequestModel
import com.basicsteps.multipos.core.model.exceptions.NotExistsException
import com.basicsteps.multipos.core.model.exceptions.ReadDbFailedException
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.model.StatusMessages
import com.basicsteps.multipos.model.entities.UnitCategoryEntity
import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

class UnitHandler(vertx: Vertx) : BaseCRUDHandler(vertx) {

    fun getUnitList(message: Message<String>) {
        if (message.body() != null) {
            var requestModel = RequestModel()

                val jsonObject = JsonObject(message.body())
                if (jsonObject.getJsonObject("params") != null)
                    requestModel = JsonUtils.toPojo(json = jsonObject.getJsonObject("params").toString())

            val result = mutableListOf<UnitCategoryEntity>()
            var count = 0
            var counter = 0
            var tempUnitCategory: UnitCategoryEntity? = null

            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            dbManager
                    .unitCategoryEntityDao
                    ?.findAll(requestModel.page, requestModel.pageSize)
                    ?.flatMapIterable({
                        count = it.count()
                        it
                    })
                    ?.flatMap({
                        tempUnitCategory = it
                        dbManager.unitEntityDao?.getUnitsByUnitCategoryId(it.id!!)
                    })
                    ?.subscribe({
                        tempUnitCategory?.unitList = it
                        result.add(tempUnitCategory?.instance() as UnitCategoryEntity)
                        counter++
                        if (count == counter) {
                            message.reply(MultiPosResponse(result, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                        }
                    }, {
                        when(it) {
                            is NotExistsException -> message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.NOT_FOUND.code()).toJson())
                            is ReadDbFailedException -> message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                            is DataStoreException -> message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        }
                    })
        }
    }

    fun getUnitById(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            findById(message, dbManager.unitEntityDao!!)
        }

    }

    fun updateUnit(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            update(message, dbManager.unitEntityDao!!)
        }
    }

}