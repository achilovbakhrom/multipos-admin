package com.basicsteps.multipos.worker.handling.handler.config

import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import com.basicsteps.multipos.core.model.exceptions.UpdateDbFailedException
import com.basicsteps.multipos.core.model.exceptions.WriteDbFailedException
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.core.response.MultiposRequest
import com.basicsteps.multipos.model.StatusMessages
import com.basicsteps.multipos.model.entities.Warehouse
import com.basicsteps.multipos.model.entities.WarehouseVsEstablishment
import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

/**
 * Created by Ikrom Mirzayev on 24-May-18.
 */
class WarehouseHandler (vertx: Vertx) : BaseCRUDHandler(vertx)  {

    fun createWarehouse(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            val warehouse = JsonUtils.toPojo<Warehouse>(json = jsonObject.getJsonObject("data").toString())
            warehouse.userId = jsonObject.getString("userId")

            val request = JsonUtils.toPojo<MultiposRequest<Warehouse>>(message.body().toString())
            dbManager
                    .warehouseDao
                    ?.save(request.data!!)
                    ?.map({
                        val warehouseVsEstList = mutableListOf<WarehouseVsEstablishment>()
                        for (item in it.establishmentIds){
                            val element = WarehouseVsEstablishment()
                            element.warehouseId = item
                            element.establishmentId = it.id!!
                            warehouseVsEstList.add(element)
                        }
                        warehouseVsEstList
                    })
                    ?.flatMap ({
                        dbManager.warehouseVsEstablishmentDao?.saveAll(it, warehouse.userId!!)
                    })
                    ?.subscribe({ result ->
                        message.reply(MultiPosResponse<Any>(result, null, "OK", HttpResponseStatus.OK.code()).toJson())
                    }, { error ->
                        when (error) {
                            is WriteDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                            is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        }
                    })
        }

    }

    fun trashWarehouse(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            val request = JsonUtils.toPojo<MultiposRequest<Warehouse>>(message.body().toString())
            dbManager
                    .warehouseDao
                    ?.trash(request.data!!)
                    ?.subscribe({ result ->
                        message.reply(MultiPosResponse<Any>(null, null, "OK", HttpResponseStatus.OK.code()).toJson())
                    }, { error ->

                    })
        }
    }

    fun updateWarehouse(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            val warehouse = JsonUtils.toPojo<Warehouse>(json = jsonObject.getJsonObject("data").toString())
            warehouse.userId = jsonObject.getString("userId")

            dbManager
                    .warehouseDao
                    ?.update(warehouse)
                    ?.map({
                        val establishmentIds = mutableListOf<String>()
                        for (item in it.establishmentIds){
                            /*
                            val element = WarehouseVsEstablishment()
                            element.warehouseId = item
                            element.establishmentId = it.id!!
                            */
                            establishmentIds.add(item)
                        }
                        establishmentIds
                    })
                    ?.flatMap ({
                        //                        dbManager.warehouseVsEstablishmentDao?.saveAll(it, establishment.userId!!)
                        dbManager.warehouseVsEstablishmentDao?.updateEstablishmentListForWarehouse(warehouse.id!!, it, warehouse.userId!!)
                    })
                    ?.subscribe({
                        message.reply(MultiPosResponse(warehouse, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    }, {
                        when (it) {
                            is DataStoreException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                            is WriteDbFailedException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                            is UpdateDbFailedException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                        }
                    })


//            val request = JsonUtils.toPojo<MultiposRequest<Warehouse>>(message.body().toString())
//            dbManager
//                    .warehouseDao
//                    ?.update(request.data!!)
//                    ?.subscribe({ result ->
//                        message.reply(MultiPosResponse<Any>(null, null, "OK", HttpResponseStatus.OK.code()).toJson())
//                    }, { error ->
//
//                    })
        }
    }

    fun getWarehouseList(message: Message<String>) {

    }

    fun getWarehouseById(message: Message<String>) {

    }

    fun getWarehousesByPOSId(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            val request = JsonUtils.toPojo<MultiposRequest<String>>(message.body().toString())
            val posId = request.data
            dbManager
                    .warehouseDao
                    ?.findAll()
                    ?.subscribe({ result ->
                        val res = mutableListOf<Warehouse>()

                        message.reply(MultiPosResponse<List<Warehouse>>(res, null, "OK", HttpResponseStatus.OK.code()).toJson())
                    }, { error ->

                    })
        }
    }

    fun getWarehousesByEstablishmentId(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            val request = JsonUtils.toPojo<MultiposRequest<String>>(message.body().toString())
            val establishmentId = request.data
            dbManager
                    .warehouseDao
                    ?.findAll()
                    ?.subscribe({ result ->
                        val res = mutableListOf<Warehouse>()

                        message.reply(MultiPosResponse<List<Warehouse>>(res, null, "OK", HttpResponseStatus.OK.code()).toJson())
                    }, { error ->

                    })
        }
    }

}