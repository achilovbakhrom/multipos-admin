package com.basicsteps.multipos.worker.handling.handler.config

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import com.basicsteps.multipos.core.model.RequestModel
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.model.StatusMessages
import com.basicsteps.multipos.model.entities.Establishment
import com.basicsteps.multipos.utils.JsonUtils
import com.basicsteps.multipos.utils.delete
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import java.util.*

class EstablishmentHandler(vertx: Vertx) : BaseCRUDHandler(vertx) {


    fun createEstablishment(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            save(message, dbManager.establishmentDao!!)
        }
    }

    fun updateEstablishment(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            update(message, dbManager.establishmentDao!!)
        }
    }

    fun getEstablishmentList(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            var requestModel = RequestModel()
            if (message.body() != null) {
                if (jsonObject.getJsonObject("params") != null)
                    requestModel = JsonUtils.toPojo(json = jsonObject.getJsonObject("params").toString())
            }
            val result = mutableListOf<Establishment>()
            var count = 0
            var counter = 0

            dbManager
                    .establishmentDao
                    ?.findAll(requestModel.page, requestModel.pageSize)
                    ?.flatMapIterable({
                        count = it.count()
                        if (count == 0) {
                            message.reply(MultiPosResponse(null, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                        }
                        it
                    })
                    ?.map({
                        result.add(it)
                        it.posIds
                    })
                    ?.flatMap({
                        dbManager.posDao?.getPOSListByIds(it)
                    })
                    ?.subscribe({
                        result[result.count() - 1].posList = it
                        counter++
                        if (count == counter) {
                            message.reply(MultiPosResponse(result, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                        }
                    })


        }
    }

    fun getEstablishmentById(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            var requestModel = RequestModel()
            if (jsonObject.getJsonObject("params") != null)
                requestModel = JsonUtils.toPojo(json = jsonObject.getJsonObject("params").toString())

            var result = Establishment()
            dbManager
                    .establishmentDao
                    ?.findById(requestModel.id)
                    ?.flatMap({
                        result = it
                        dbManager.posDao?.getPOSListByIds(it.posIds)
                    })
                    ?.subscribe({
                        result.posList = it
                        message.reply(MultiPosResponse(result, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    })


        }
    }

    fun deleteEstablishment(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            trash(message, dbManager.establishmentDao!!)
        }
    }

}