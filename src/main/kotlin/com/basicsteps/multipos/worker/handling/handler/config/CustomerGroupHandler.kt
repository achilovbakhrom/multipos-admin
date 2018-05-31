package com.basicsteps.multipos.worker.handling.handler.config


import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import com.basicsteps.multipos.core.model.RequestModel
import com.basicsteps.multipos.core.model.exceptions.UpdateDbFailedException
import com.basicsteps.multipos.core.model.exceptions.WriteDbFailedException
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.model.StatusMessages
import com.basicsteps.multipos.model.entities.CustomerGroup
import com.basicsteps.multipos.model.entities.CustomerVsCustomerGroup
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

class CustomerGroupHandler(vertx: Vertx) : BaseCRUDHandler(vertx) {


    fun createCustomerGroup(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            val customerGroup = JsonUtils.toPojo<CustomerGroup>(json = jsonObject.getJsonObject("data").toString())
            customerGroup.userId = jsonObject.getString("userId")

            dbManager
                    .customerGroupDao
                    ?.save(customerGroup)
                    ?.map({
                        val customerVsCustomerGroup = mutableListOf<CustomerVsCustomerGroup>()
                        for (item in it.customersIdList!!){
                            val element = CustomerVsCustomerGroup()
                            element.customerId = item
                            element.customerGroupId = it.id!!
                            customerVsCustomerGroup.add(element)
                        }
                        customerVsCustomerGroup
                    })
                    ?.flatMap ({
                        dbManager.customerGroupVsCustomerDao?.saveAll(it, customerGroup.userId!!)
                    })
                    ?.subscribe({
                        message.reply(MultiPosResponse(customerGroup, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    }, {
                        when (it) {
                            is DataStoreException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                            is WriteDbFailedException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                        }
                    })
        }
    }

    fun updateCustomerGroup(message: Message<String>) {

        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            val customerGroup = JsonUtils.toPojo<CustomerGroup>(json = jsonObject.getJsonObject("data").toString())
            customerGroup.userId = jsonObject.getString("userId")

            dbManager
                    .customerGroupDao
                    ?.update(customerGroup)
                    ?.map({
                        val customerIds = mutableListOf<String>()
                        for (item in it.customersIdList!!){
                            /*
                            val element = WarehouseVsEstablishment()
                            element.warehouseId = item
                            element.establishmentId = it.id!!
                            */
                            customerIds.add(item)
                        }
                        customerIds
                    })
                    ?.flatMap ({
                        //                        dbManager.warehouseVsEstablishmentDao?.saveAll(it, establishment.userId!!)
                        dbManager.customerGroupVsCustomerDao?.updateCustomerListForCustomerGroup(customerGroup.id!!, it, customerGroup.userId!!)
                    })
                    ?.subscribe({
                        message.reply(MultiPosResponse(customerGroup, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
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

    fun getCustomerGroupList(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            var requestModel = RequestModel()
            if (message.body() != null) {
                if (jsonObject.getJsonObject("params") != null)
                    requestModel = JsonUtils.toPojo(json = jsonObject.getJsonObject("params").toString())
            }
            val result = mutableListOf<CustomerGroup>()
            var count = 0
            var counter = 0

            dbManager
                    .customerGroupDao
                    ?.findAll(requestModel.page, requestModel.pageSize)
                    ?.flatMapIterable({
                        count = it.count()
                        if (count == 0) {
                            message.reply(MultiPosResponse(null, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                        }
                        it
                    })
                    ?.flatMap ({
                        dbManager.customerGroupVsCustomerDao?.findCustomerIdsByCustomerGroupId(result[result.count() - 1].id!!)
                    })
                    ?.subscribe({
                        val customerIdList = mutableListOf<String>()
                        for (elememt in it) {
                            customerIdList.add(element = elememt.customerId)
                        }
                        result[result.count() - 1].customersIdList = customerIdList
                        counter++
                        if (count == counter) {
                            message.reply(MultiPosResponse(result, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                        }
                    })
        }
    }

    fun getCustomerGroupById(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            var requestModel = RequestModel()
            if (jsonObject.getJsonObject("params") != null)
                requestModel = JsonUtils.toPojo(json = jsonObject.getJsonObject("params").toString())

            var result = CustomerGroup()
            dbManager
                    .customerGroupDao
                    ?.findById(requestModel.id)
                    ?.subscribe({
                        result = it
                        message.reply(MultiPosResponse(result, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    })


        }
    }

    fun deleteCustomerGroup(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            trash(message, dbManager.customerGroupDao!!)
        }
    }

}