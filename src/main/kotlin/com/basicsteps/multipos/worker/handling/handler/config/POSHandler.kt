package com.basicsteps.multipos.worker.handling.handler.config

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import com.basicsteps.multipos.core.model.RequestModel
import com.basicsteps.multipos.core.model.exceptions.NotExistsException
import com.basicsteps.multipos.core.model.exceptions.ReadDbFailedException
import com.basicsteps.multipos.core.model.exceptions.WriteDbFailedException
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.core.response.MultiposRequest
import com.basicsteps.multipos.model.StatusMessages
import com.basicsteps.multipos.model.entities.*
import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject


class POSHandler(vertx: Vertx) : BaseCRUDHandler(vertx) {

    fun createPOS(message: Message<String>) {

        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())

            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            val dao = dbManager.posDao!!

            val pos = JsonUtils.toPojo<POS>(jsonObject.getJsonObject("data").getJsonObject("pos").toString())
            pos.userId = jsonObject.getString("userId")
            val stockIds = JsonUtils.toPojo<List<String>>(jsonObject.getJsonObject("data").getJsonArray("stock_ids").toString())
            var tempPOS: POS? = null
            val count = stockIds.count()
            var counter = 0

            dao
                    .save(pos)
                    .map({
                        tempPOS = it
                        stockIds
                    }).flatMapIterable({ it })
                    .flatMap({
                        val result = StockVsPOS()
                        result.posId = tempPOS?.id!!
                        result.stockId = it
                        dbManager.stockVsPOSDao?.save(result)
                    })
                    .subscribe({
                        counter++
                        if (count == counter) {
                            val result = JsonObject()
                            result.put("stock_ids", stockIds)
                            result.put("pos", JsonUtils.toJsonObject(tempPOS))
                            message.reply(MultiPosResponse(result, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                        }
                    }, {
                        when(it) {
                            is WriteDbFailedException -> message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                            is DataStoreException -> message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        }
                    })
        }
    }

    fun getPOSList(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            val dao = dbManager.posDao
            var requestModel = RequestModel()
            if (jsonObject.getJsonObject("params") != null)
                requestModel = JsonUtils.toPojo(json = jsonObject.getJsonObject("params").toString())
            val result = mutableListOf<POSResponse>()
            var count = 0
            var counter = 0
            var tempPos: POS? = null
            dao
                    ?.findAll(requestModel.page, requestModel.pageSize)
                    ?.flatMapIterable({
                        if (it.isEmpty()) {
                            message.reply(MultiPosResponse(listOf<Any>(), null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                        }
                        count = it.count()
                        it
                    })
                    ?.flatMap({
                        tempPos = it
                        dbManager.stockVsPOSDao?.findStockIdsByPOSId(it.id!!)
                    })
                    ?.subscribe({
                        val tempRes = mutableListOf<String>()
                        for (element in it) {
                            tempRes.add(element.stockId)
                        }
                        val res = POSResponse(tempPos!!, tempRes)
                        result.add(res)
                        counter++
                        if (count == counter) {
                            message.reply(MultiPosResponse(result, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                        }
                    }, {
                        when (it) {
                            is ReadDbFailedException -> message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                            is DataStoreException -> message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        }
                    })
        }


    }

    fun getPOSById(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            val dao = dbManager.posDao
            var request = RequestModel()
            if (jsonObject.getJsonObject("params") != null)
                request = JsonUtils.toPojo(json = jsonObject.getJsonObject("params").toString())
            var tempPOS: POS? = null
            dao
                    ?.findById(request.id)
                    ?.flatMap({
                        tempPOS = it
                        dbManager.stockVsPOSDao?.findStockIdsByPOSId(it.id!!)
                    })
                    ?.subscribe({
                        val tempRes = mutableListOf<String>()
                        for (element in it) {
                            tempRes.add(element.stockId)
                        }
                        val result = POSResponse(tempPOS!!, tempRes)
                        message.reply(MultiPosResponse(result, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    }, {
                        when (it) {
                            is NotExistsException -> message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.NOT_FOUND.code()).toJson())
                            is ReadDbFailedException -> message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                            is DataStoreException -> message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        }
                    })
        }

    }

    fun updatePOS(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            update(message, dbManager.posDao!!)
        }
    }

    fun trashPOS(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            trash(message, dbManager.posDao!!)
        }
    }

    fun createEstablishment(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            val request = JsonUtils.toPojo<MultiposRequest<Establishment>>(message.body().toString())
            dbManager
                    .establishmentDao
                    ?.save(request.data!!)
                    ?.subscribe({ result ->
                        message.reply(MultiPosResponse<Any>(null, null, "OK", HttpResponseStatus.OK.code()))
                    }, { error ->

                    })
        }
    }

    fun trashEstablishment(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            val request = JsonUtils.toPojo<MultiposRequest<Establishment>>(message.body().toString())
            dbManager
                    .establishmentDao
                    ?.trash(request.data!!)
                    ?.subscribe({ result ->
                        message.reply(MultiPosResponse<Any>(null, null, "OK", HttpResponseStatus.OK.code()))
                    }, { error ->

                    })
        }
    }

    fun updateEstablishment(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            val request = JsonUtils.toPojo<MultiposRequest<Establishment>>(message.body().toString())
            dbManager
                    .establishmentDao
                    ?.update(request.data!!)
                    ?.subscribe({ result ->
                        message.reply(MultiPosResponse<Any>(null, null, "OK", HttpResponseStatus.OK.code()))
                    }, { error ->

                    })
        }
    }

    fun getEstablishmentList(message: Message<String>) {

    }


    fun createStock(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            val request = JsonUtils.toPojo<MultiposRequest<Stock>>(message.body().toString())
            dbManager
                    .stockDao
                    ?.save(request.data!!)
                    ?.subscribe({ result ->
                        message.reply(MultiPosResponse<Any>(result, null, "OK", HttpResponseStatus.OK.code()))
                    }, { error ->
                        when (error) {
                            is WriteDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                            is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        }
                    })
        }

    }

    fun trashStock(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            val request = JsonUtils.toPojo<MultiposRequest<Stock>>(message.body().toString())
            dbManager
                    .stockDao
                    ?.trash(request.data!!)
                    ?.subscribe({ result ->
                        message.reply(MultiPosResponse<Any>(null, null, "OK", HttpResponseStatus.OK.code()))
                    }, { error ->

                    })
        }
    }

    fun updateStock(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            val request = JsonUtils.toPojo<MultiposRequest<Stock>>(message.body().toString())
            dbManager
                    .stockDao
                    ?.update(request.data!!)
                    ?.subscribe({ result ->
                        message.reply(MultiPosResponse<Any>(null, null, "OK", HttpResponseStatus.OK.code()))
                    }, { error ->

                    })
        }
    }

    fun getStockList(message: Message<String>) {

    }

    fun getStockById(message: Message<String>) {

    }

    fun getStocksByPOSId(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            val request = JsonUtils.toPojo<MultiposRequest<String>>(message.body().toString())
            val posId = request.data
            dbManager
                    .stockDao
                    ?.findAll()
                    ?.subscribe({ result ->
                        val res = mutableListOf<Stock>()

                        message.reply(MultiPosResponse<List<Stock>>(res, null, "OK", HttpResponseStatus.OK.code()))
                    }, { error ->

                    })
        }
    }

    fun getStocksByEstablishmentId(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            val request = JsonUtils.toPojo<MultiposRequest<String>>(message.body().toString())
            val establishmentId = request.data
            dbManager
                    .stockDao
                    ?.findAll()
                    ?.subscribe({ result ->
                        val res = mutableListOf<Stock>()

                        message.reply(MultiPosResponse<List<Stock>>(res, null, "OK", HttpResponseStatus.OK.code()))
                    }, { error ->

                    })
        }
    }

}