package com.basicsteps.multipos.worker.handling.handler.config

import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import com.basicsteps.multipos.core.model.exceptions.UpdateDbFailedException
import com.basicsteps.multipos.core.model.exceptions.WriteDbFailedException
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.core.response.MultiposRequest
import com.basicsteps.multipos.model.StatusMessages
import com.basicsteps.multipos.model.entities.Customer
import com.basicsteps.multipos.model.entities.CustomerVsCustomerGroup
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
class CustomerHandler (vertx: Vertx) : BaseCRUDHandler(vertx)  {

    fun createCustomer(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            val customer = JsonUtils.toPojo<Customer>(json = jsonObject.getJsonObject("data").toString())
            customer.userId = jsonObject.getString("userId")

            val request = JsonUtils.toPojo<MultiposRequest<Customer>>(message.body().toString())
            dbManager
                    .customerDao
                    ?.save(request.data!!)
                    ?.map({
                        val customerVsCustomerGroupList = mutableListOf<CustomerVsCustomerGroup>()
                        for (item in it.customerGroupListIds!!){
                            val element = CustomerVsCustomerGroup()
                            element.customerId = item
                            element.customerGroupId = it.id!!
                            customerVsCustomerGroupList.add(element)
                        }
                        customerVsCustomerGroupList
                    })
                    ?.flatMap ({
                        dbManager.customerGroupVsCustomerDao?.saveAll(it, customer.userId!!)
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

    fun trashCustomer(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            val request = JsonUtils.toPojo<MultiposRequest<Customer>>(message.body().toString())
            dbManager
                    .customerDao
                    ?.trash(request.data!!)
                    ?.subscribe({ result ->
                        message.reply(MultiPosResponse<Any>(null, null, "OK", HttpResponseStatus.OK.code()).toJson())
                    }, { error ->

                    })
        }
    }

    fun updateCustomer(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            val customer = JsonUtils.toPojo<Customer>(json = jsonObject.getJsonObject("data").toString())
            customer.userId = jsonObject.getString("userId")

            dbManager
                    .customerDao
                    ?.update(customer)
                    ?.map({
                        val customerGroupIds = mutableListOf<String>()
                        for (item in it.customerGroupListIds!!){
                            /*
                            val element = WarehouseVsEstablishment()
                            element.warehouseId = item
                            element.establishmentId = it.id!!
                            */
                            customerGroupIds.add(item)
                        }
                        customerGroupIds
                    })
                    ?.flatMap ({
                        //                        dbManager.warehouseVsEstablishmentDao?.saveAll(it, establishment.userId!!)
                        dbManager.customerGroupVsCustomerDao?.updateCustomerGroupListForCustomer(customer.id!!, it, customer.userId!!)
                    })
                    ?.subscribe({
                        message.reply(MultiPosResponse(customer, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
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

    fun getCustomerList(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            findAll(message, dbManager.customerDao!!)
        }
    }

    fun getCustomerById(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            findById(message, dbManager.customerDao!!)
        }
    }

}