package com.basicsteps.multipos.core.handler

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.RequestModel
import com.basicsteps.multipos.core.model.exceptions.*
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.core.response.MultiposRequest
import com.basicsteps.multipos.model.StatusMessages
import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.Observable
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

/**
 *      {
 *          "userId" : "basicsteps@gmail.com",
 *          "params" : {
 *              "id" : 123,
 *              "page" : 0,
 *              "page_size" : 15
 *          },
 *          "data": {array|object}
 *      }
 */


abstract class BaseCRUDHandler(val vertx : Vertx) {

    inline fun <reified T: BaseModel> save(message: Message<String>, dao: BaseDao<T>) {
        val jsonObject = JsonObject(message.body())
        val request = JsonUtils.toPojo<T>(json = jsonObject.getJsonObject("data").toString())
        request.userId = jsonObject.getString("userId")
        dao
                .save(request)
                .subscribe({ item ->
                    message.reply(MultiPosResponse(item, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                }, { error ->
                    when (error) {
                        is WriteDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }

    inline fun <reified T: BaseModel> saveAll(message: Message<String>, dao: BaseDao<T>) {
        val jsonObject = JsonObject(message.body())
        val userId = jsonObject.getString("userId")
        val request = JsonUtils.toPojo<List<T>>(json = jsonObject.getJsonArray("data").toString())
        dao
                .saveAll(request, userId = userId)
                .subscribe({ items ->
                    message.reply(MultiPosResponse(items, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                }, { error ->
                    when (error) {
                        is WriteDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }

    inline fun <reified T: BaseModel> findById(message: Message<String>, dao: BaseDao<T>) {
        val jsonObject = JsonObject(message.body())
        var request = RequestModel()
        if (jsonObject.getJsonObject("params") != null)
            request = JsonUtils.toPojo(json = jsonObject.getJsonObject("params").toString())
        dao
                .findById(request.id)
                .subscribe({item ->
                    message.reply(MultiPosResponse(item, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                }, { error ->
                    when (error) {
                        is NotExistsException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.NOT_FOUND.code()).toJson())
                        is ReadDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }


    inline fun <reified T: BaseModel> findAll(message: Message<String>, dao: BaseDao<T>) {
        var requestModel = RequestModel()
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            if (jsonObject.getJsonObject("params") != null)
                requestModel = JsonUtils.toPojo(json = jsonObject.getJsonObject("params").toString())
        }


        dao
                .findAll(requestModel.page, requestModel.pageSize)
                .subscribe({ items ->
                    message.reply(MultiPosResponse(items, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                }, { error ->
                    when (error) {
                        is ReadDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }

    inline fun <reified T: BaseModel> findAllById(message: Message<String>, dao: BaseDao<T>) {

        val jsonObject = JsonObject(message.body())
        val ids = JsonUtils.toPojo<List<String>>(jsonObject.getJsonObject("data").toString())
        if (ids.isEmpty()) {
            message.reply(MultiPosResponse(null, "Id list cannot be empty", StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
            return
        }
        val result = mutableListOf<T>()
        val count = ids.count()
        var counter = 0
        Observable
                .fromArray(ids)
                .flatMapIterable({list -> list })
                .flatMap({ id -> dao.findById(id) })
                .subscribe({item ->
                    result.add(item)
                    if (counter == count - 1) {
                        message.reply(MultiPosResponse(result, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    }
                    counter++
                }, { error ->
                    when (error) {
                        is NotExistsException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.NOT_FOUND.code()).toJson())
                        is ReadDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }

    inline fun <reified T: BaseModel> update(message: Message<String>, dao: BaseDao<T>) {
        val jsonObject = JsonObject(message.body())
        val request = JsonUtils.toPojo<T>(json = jsonObject.getJsonObject("data").toString())
        request.userId = jsonObject.getString("userId")
        dao
                .update(request)
                .subscribe({ item ->
                    message.reply(MultiPosResponse(item, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                }, { error ->
                    when (error) {
                        is UpdateDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }

    inline fun <reified T: BaseModel> updateAll(message: Message<String>, dao: BaseDao<T>) {
        val jsonObject = JsonObject(message.body())
        val userId = jsonObject.getString("userId")
        val list = JsonUtils.toPojo<List<T>>(message.body())
        dao
                .updateAll(list, userId = userId)
                .subscribe({ items ->
                    message.reply(MultiPosResponse(items, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                }, { error ->
                    when (error) {
                        is UpdateDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }

    inline fun <reified T: BaseModel> trash(message: Message<String>, dao: BaseDao<T>) {
        val jsonObject = JsonObject(message.body())
        val request = JsonUtils.toPojo<MultiposRequest<T>>(jsonObject.getJsonObject("data").toString())
        request.userId = jsonObject.getString("userId")
        dao
                .trash(request.data!!)
                .subscribe({item ->
                    message.reply(MultiPosResponse(item, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                }, {error ->
                    when (error) {
                        is WriteDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }

    inline fun <reified T: BaseModel> trashById(message: Message<String>, dao: BaseDao<T>) {
        val jsonObject = JsonObject(message.body())
        val userId = jsonObject.getString("userId")
        val requestModel = JsonUtils.toPojo<RequestModel>(jsonObject.getJsonObject("params").toString())
        dao
                .trash(requestModel.id, userId = userId)
                .subscribe({item ->
                    message.reply(MultiPosResponse(item, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                }, { error ->
                    when (error) {
                        is NotExistsException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.NOT_FOUND.code()).toJson())
                        is WriteDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }

    inline fun <reified T: BaseModel> trashAll(message: Message<String>, dao: BaseDao<T>) {
        val jsonObject = JsonObject(message.body())
        val userId = jsonObject.getString("userId")
        val list = JsonUtils.toPojo<List<T>>(jsonObject.getJsonObject("data").toString())

        if (list.isEmpty()) {
            message.reply(MultiPosResponse(null, "List cannot be empty", StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
            return
        }
        val result = mutableListOf<T>()
        val count = list.count()
        var counter = 0
        Observable
                .fromArray(list)
                .flatMapIterable({ list -> list})
                .flatMap({ item ->
                    item.userId = userId
                    dao.trash(item)
                })
                .subscribe({ item ->
                    result.add(item)
                    if (counter == count - 1) {
                        message.reply(MultiPosResponse(result, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    }
                    counter++
                }, {error ->
                    when (error) {
                        is WriteDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }

    inline fun <reified T: BaseModel> trashAllById(message: Message<String>, dao: BaseDao<T>) {
        val jsonObject = JsonObject(message.body())
        val userId = jsonObject.getString("userId")
        val list = JsonUtils.toPojo<List<String>>(json = jsonObject.getJsonObject("data").toString())

        if (list.isEmpty()) {
            message.reply(MultiPosResponse(null, "Id list cannot be empty", StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
            return
        }
        val result = mutableListOf<T>()
        val count = list.count()
        var counter = 0
        Observable
                .fromArray(list)
                .flatMapIterable({ list -> list})
                .flatMap({ item -> dao.trash(item, userId) })
                .subscribe({ item ->
                    result.add(item)
                    if (counter == count - 1) {
                        message.reply(MultiPosResponse(result, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    }
                    counter++
                }, {error ->
                    when (error) {
                        is WriteDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }

    inline fun <reified T: BaseModel> remove(message: Message<String>, dao: BaseDao<T>) {
        val jsonObject = JsonObject(message.body())
        val request = JsonUtils.toPojo<T>(json = jsonObject.getJsonObject("data").toString())
        dao
                .delete(request)
                .subscribe({item ->
                    message.reply(MultiPosResponse(item, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                }, {error ->
                    when (error) {
                        is DeleteDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }

    inline fun <reified T: BaseModel> removeById(message: Message<String>, dao: BaseDao<T>) {
        val jsonObject = JsonObject(message.body())
        val requestModel = JsonUtils.toPojo<RequestModel>(json = jsonObject.getJsonObject("params").toString())

        dao
                .findById(requestModel.id)
                .flatMap({item ->
                    dao.delete(item)
                })
                .subscribe({deleted ->
                    message.reply(MultiPosResponse(deleted, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                }, {error ->
                    when (error) {
                        is DeleteDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }

    inline fun <reified T: BaseModel> removeAll(message: Message<String>, dao: BaseDao<T>) {
        val jsonObject = JsonObject(message.body())
        val list = JsonUtils.toPojo<List<T>>(json = jsonObject.getJsonArray("data").toString())
        if (list.isEmpty()) {
            message.reply(MultiPosResponse(null, "List cannot be empty", StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
            return
        }
        val count = list.count()
        var counter = 0
        Observable
                .fromArray(list)
                .flatMapIterable({ list -> list})
                .flatMap({ item -> dao.delete(item) })
                .subscribe({ deleted ->
                    if (counter == count - 1) {
                        message.reply(MultiPosResponse(deleted, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    }
                    counter++
                }, {error ->
                    when (error) {
                        is DeleteDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }

    inline fun <reified T: BaseModel> removeAllById(message: Message<String>, dao: BaseDao<T>) {
        val jsonObject = JsonObject(message.body())
        val list = JsonUtils.toPojo<List<String>>(json = jsonObject.getJsonArray("data").toString())
        if (list.isEmpty()) {
            message.reply(MultiPosResponse(null, "List cannot be empty", StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
            return
        }
        val count = list.count()
        var counter = 0
        Observable
                .fromArray(list)
                .flatMapIterable({ list -> list})
                .flatMap({ id -> dao.findById(id) })
                .flatMap({ item -> dao.delete(item) })
                .subscribe({ deleted ->
                    if (counter == count - 1) {
                        message.reply(MultiPosResponse(deleted, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    }
                    counter++
                }, {error ->
                    when (error) {
                        is DeleteDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }

    fun getDbManagerByTenantId(tenantId: String?) : DbManager {
        val result = DbManager(vertx)
        if (tenantId != null)
            result.setMongoClientByTenantId(tenantId = tenantId)
        return result
    }


}
