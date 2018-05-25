package com.basicsteps.multipos.worker.handling.handler.config

import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import com.basicsteps.multipos.core.model.RequestModel
import com.basicsteps.multipos.core.model.exceptions.UpdateDbFailedException
import com.basicsteps.multipos.core.model.exceptions.WriteDbFailedException
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.core.response.MultiposRequest
import com.basicsteps.multipos.model.StatusMessages
import com.basicsteps.multipos.model.entities.Product
import com.basicsteps.multipos.model.entities.ProductToTax
import com.basicsteps.multipos.model.entities.Tax
import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

class ProductHandler(vertx: Vertx) : BaseCRUDHandler(vertx) {

    fun getProductList(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            findAll(message, dao = dbManager.productDao!!)
        }
    }

    fun createProduct(message: Message<String>) {
        if (message.body() != null) {

            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            val product = JsonUtils.toPojo<Product>(json = jsonObject.getJsonObject("data").toString())
            product.userId = jsonObject.getString("userId")

            val request = JsonUtils.toPojo<MultiposRequest<Product>>(message.body().toString())
            request.userId = jsonObject.getString("userId")
            dbManager
                    .productDao
                    ?.save(request.data!!)
                    ?.map({
                        val warehouseVsEstList = mutableListOf<ProductToTax>()
                        for (item in it.taxIds!!){
                            val element = ProductToTax()
                            element.taxId = item
                            element.productId = it.id!!
                            warehouseVsEstList.add(element)
                        }
                        warehouseVsEstList
                    })
                    ?.flatMap ({
                        dbManager.productToTaxDao?.saveAll(it, product.userId!!)
                    })
                    ?.subscribe({ result ->
                        message.reply(MultiPosResponse<Any>(product, null, "OK", HttpResponseStatus.OK.code()).toJson())
                    }, { error ->
                        when (error) {
                            is WriteDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                            is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        }
                    })

//            save(message, dao = dbManager.productDao!!)
        }
    }

    fun updateProduct(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            val product = JsonUtils.toPojo<Product>(json = jsonObject.getJsonObject("data").toString())
            product.userId = jsonObject.getString("userId")

            dbManager
                    .productDao
                    ?.update(product)
                    ?.map({
                        val establishmentIds = mutableListOf<String>()
                        for (item in it.taxIds!!){
                            establishmentIds.add(item)
                        }
                        establishmentIds
                    })
                    ?.flatMap ({
                        dbManager.productToTaxDao?.updateTaxListForProduct(product.id!!, it, product.userId!!)
                    })
                    ?.subscribe({
                        message.reply(MultiPosResponse(product, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    }, {
                        when (it) {
                            is DataStoreException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                            is WriteDbFailedException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                            is UpdateDbFailedException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                        }
                    })

//            update(message, dao = dbManager.productDao!!)
        }
    }

    fun getProductById(message: Message<String>) {
        if (message.body() != null) {

            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            var requestModel = RequestModel()
            if (jsonObject.getJsonObject("params") != null)
                requestModel = JsonUtils.toPojo(json = jsonObject.getJsonObject("params").toString())

            var result = Product()
            dbManager
                    .productDao
                    ?.findById(requestModel.id)
                    ?.flatMap({
                        result = it
                        dbManager.taxDao?.getTaxListByIds(it.taxIds!!)
                    })
                    ?.subscribe({
                        result.taxes = it
                        message.reply(MultiPosResponse(result, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    })

        }
    }

    fun trashProductById(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            trashById(message, dao = dbManager.productDao!!)
        }
    }

    fun getTaxByProductId(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

//            val request = JsonUtils.toPojo<MultiposRequest<String>>(message.body().toString())
            dbManager
                    .taxDao
                    ?.findAll()
                    ?.subscribe({ result ->
                        val res = mutableListOf<Tax>()

                        message.reply(MultiPosResponse<List<Tax>>(res, null, "OK", HttpResponseStatus.OK.code()).toJson())
                    }, { error ->

                    })
        }
    }

}