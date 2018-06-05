package com.basicsteps.multipos.worker.handling.handler.config


import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.model.exceptions.NotExistsException
import com.basicsteps.multipos.core.model.exceptions.ReadDbFailedException
import com.basicsteps.multipos.core.model.exceptions.UpdateDbFailedException
import com.basicsteps.multipos.core.model.exceptions.WriteDbFailedException
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.core.response.MultiposRequest
import com.basicsteps.multipos.model.DocumentType
import com.basicsteps.multipos.model.StatusMessages
import com.basicsteps.multipos.model.entities.Invoice
import com.basicsteps.multipos.model.entities.Order
import com.basicsteps.multipos.model.entities.Payment
import com.basicsteps.multipos.model.entities.UnitEntity
import com.basicsteps.multipos.utils.JsonUtils
import com.basicsteps.multipos.worker.handling.dao.UnitEntityDao
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.Observable
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

class PaymentHandler(vertx: Vertx) : BaseCRUDHandler(vertx) {


    fun getPaymentList(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            findAll(message, dbManager.paymentDao!!)
        }
    }

    fun createPayment(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            val payment = JsonUtils.toPojo<Payment>(json = jsonObject.getJsonObject("data").toString())
            payment.userId = jsonObject.getString("userId")
//            var ref = mutableListOf<IncomingProduct>()

            dbManager
                    .paymentDao
                    ?.save(payment)
                    ?.flatMap({
                        if (it.documentType == DocumentType.PURCHASE.value()) {
                            dbManager.invoiceDao?.findById(payment.sourceId!!)

                        } else {
                            dbManager.orderDao?.findById(payment.sourceId!!)
                        }
                    })
                    ?.flatMap ({
                        if (it is Invoice) {
                            var listOfPayments = it.listOfPayments?.toMutableList()
                            listOfPayments?.add(payment)
                            it.listOfPayments = listOfPayments
                            dbManager.invoiceDao?.update(it)
                        } else if(it is Order) {
                            var listOfPayments = it.listOfPayments?.toMutableList()
                            listOfPayments?.add(payment)
                            it.listOfPayments = listOfPayments
                            dbManager.orderDao?.update(it)
                        } else {
                            Observable.just("")
                        }
                    })
                    ?.subscribe({
                        message.reply(MultiPosResponse(payment, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    }, {
                        when (it) {
                            is DataStoreException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                            is WriteDbFailedException -> { message.reply(MultiPosResponse(null, it.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson()) }
                        }
                    })



//            save(message, dbManager.paymentDao!!)
        }
    }

    fun deletePayment(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)

            trash(message, dbManager.paymentDao!!)
        }
    }

    fun updatePayment(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            update(message, dbManager.paymentDao!!)
        }
    }

    fun getPaymentById(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            findById(message, dbManager.paymentDao!!)
        }
    }

}