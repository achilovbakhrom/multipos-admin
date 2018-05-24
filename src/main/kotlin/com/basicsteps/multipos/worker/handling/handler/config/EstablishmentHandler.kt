package com.basicsteps.multipos.worker.handling.handler.config

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import com.basicsteps.multipos.core.model.RequestModel
import com.basicsteps.multipos.core.model.exceptions.UpdateDbFailedException
import com.basicsteps.multipos.core.model.exceptions.WriteDbFailedException
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.model.StatusMessages
import com.basicsteps.multipos.model.entities.Establishment
import com.basicsteps.multipos.model.entities.WarehouseVsEstablishment
import com.basicsteps.multipos.utils.JsonUtils
import com.basicsteps.multipos.utils.delete
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.Observable
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
            val establishment = JsonUtils.toPojo<Establishment>(json = jsonObject.getJsonObject("data").toString())
            establishment.userId = jsonObject.getString("userId")

            dbManager
                    .establishmentDao
                    ?.save(establishment)
                    ?.map({
                        val warehouseVsEstList = mutableListOf<WarehouseVsEstablishment>()
                        for (item in it.warehouseId){
                            val element = WarehouseVsEstablishment()
                            element.warehouseId = item
                            element.establishmentId = it.id!!
                            warehouseVsEstList.add(element)
                        }
                        warehouseVsEstList
                    })
                    ?.flatMap ({
                        dbManager.warehouseVsEstablishmentDao?.saveAll(it, establishment.userId!!)
                    })
                    ?.subscribe({
                        message.reply(MultiPosResponse(establishment, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    }, {
                        when (it) {
                            is DataStoreException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                            is WriteDbFailedException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                        }
                    })
        }
    }

    fun updateEstablishment(message: Message<String>) {

        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            val establishment = JsonUtils.toPojo<Establishment>(json = jsonObject.getJsonObject("data").toString())
            establishment.userId = jsonObject.getString("userId")

            dbManager
                    .establishmentDao
                    ?.update(establishment)
                    ?.map({
                        val warehouseIds = mutableListOf<String>()
                        for (item in it.warehouseId){
                            /*
                            val element = WarehouseVsEstablishment()
                            element.warehouseId = item
                            element.establishmentId = it.id!!
                            */
                            warehouseIds.add(item)
                        }
                        warehouseIds
                    })
                    ?.flatMap ({
//                        dbManager.warehouseVsEstablishmentDao?.saveAll(it, establishment.userId!!)
                        dbManager.warehouseVsEstablishmentDao?.updateWarehouseListForEstablishement(establishment.id!!, it, establishment.userId!!)
                    })
                    ?.subscribe({
                        message.reply(MultiPosResponse(establishment, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    }, {
                        when (it) {
                            is DataStoreException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                            is WriteDbFailedException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                            is UpdateDbFailedException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                        }
                    })
        }


//        if (message.body() != null) {
//            val jsonObject = JsonObject(message.body())
//            val tenantId = jsonObject.getString("tenantId")
//            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
//            update(message, dbManager.establishmentDao!!)
//        }
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
                    ?.flatMap ({
                        result[result.count() - 1].posList = it
                        dbManager.warehouseVsEstablishmentDao?.findStockIdsByEstablishmentId(result[result.count() - 1].id!!)
                    })
                    ?.subscribe({
                        val warehouseIdList = mutableListOf<String>()
                        for (elememt in it) {
                            warehouseIdList.add(element = elememt.warehouseId)
                        }
                        result[result.count() - 1].warehouseId = warehouseIdList
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