package com.basicsteps.multipos.worker.handling.handler.company

import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import com.basicsteps.multipos.core.model.exceptions.*
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.core.response.MultiposRequest
import com.basicsteps.multipos.model.*
import com.basicsteps.multipos.model.entities.Account
import com.basicsteps.multipos.utils.JsonUtils
import com.basicsteps.multipos.utils.SystemConfig
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

class CompanyHandler(vertx: Vertx): BaseCRUDHandler(vertx) {

    fun createCompany(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            val request= JsonUtils.toPojo<MultiposRequest<Company>>(message.body().toString())

            val company = request.data
            if (company == null) {
                message.reply(
                        MultiPosResponse(DataNotFoundException(),
                                ErrorMessages.COMPANY_NOT_FOUND.value(),
                                StatusMessages.ERROR.value(),
                                HttpResponseStatus.BAD_REQUEST.code()
                        ).toJson()
                )
            } else {
                dbManager
                        .tenantsDao
                        ?.generateTenant()
                        ?.subscribe({item ->
                            company.tenantId = item
                            company.userId = request.userId
                            dbManager
                                    .companyDao
                                    ?.save(company)
                                    ?.subscribe({ company ->
                                        dbManager.setMongoClientByTenantId(item)
                                        prepareCompanyData(request.userId!!, item, company.id!!)
                                        message.reply(MultiPosResponse(company, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                                    }, {error ->
                                        when (error) {
                                            is WriteDbFailedException -> {
                                                message.reply(MultiPosResponse(null, error.message,
                                                        StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                                            }
                                            is DataStoreException -> {
                                                message.reply(MultiPosResponse(null, error.message,
                                                        StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                                            }
                                        }
                                    })


                        }, { error ->
                            when(error) {
                                is FieldConflictsException -> {message.reply(MultiPosResponse(null, ErrorMessages.COMPANY_IDENTIFIER_NOT_UNIQUE.value(),
                                        StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())}
                                is WriteDbFailedException -> {
                                    message.reply(MultiPosResponse(null, error.message,
                                            StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                                }
                                is DataStoreException -> {
                                    message.reply(MultiPosResponse(null, error.message,
                                            StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                                }
                                else -> {
                                    message.reply(MultiPosResponse<Any>(null, ErrorMessages.UNKNOWN_ERROR.value(), StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
                                }
                            }
                        })
            }
        }



    }


    fun getCompanyById(message: Message<String>) {

        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            val request = JsonUtils.toPojo<MultiposRequest<String>>(message.body())
            dbManager
                    .companyDao
                    ?.findById(request.data!!)
                    ?.subscribe({ company ->
                        message.reply(MultiPosResponse(company, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    }, { error ->
                        when (error) {
                            is NotExistsException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.NOT_FOUND.code()))
                            is ReadDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()))
                            is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()))
                        }
                    })
        }



    }

    private fun prepareCompanyData(userId: String, tenantId: String, companyId: String) {
        val dbManager = getDbManagerByTenantId(tenantId = tenantId)
        SystemConfig
            .getUnits(vertx)
            .flatMap({ unitEntities -> dbManager.unitEntityDao?.saveAll(unitEntities, userId) })
            .flatMap({SystemConfig.getUnitCategories(vertx)})
            .flatMap({ unitCategories -> dbManager.unitCategoryEntityDao?.saveAll(unitCategories, userId) })
            .flatMap({ SystemConfig.getCurrencies(vertx) })
            .flatMap({ currencies -> dbManager.currencyDao?.saveAll(currencies, userId) })
            .flatMap({
                val coinAccount = Account()
                coinAccount.name = "Coin Account"
                coinAccount.type = AccountType.CASH.value()
                dbManager.accountDao?.save(coinAccount)
            })
            .map({
                val tenantsObject = Tenants()
                tenantsObject.tenant = tenantId
                tenantsObject.companyIdentifier = companyId
                tenantsObject
            })
            .flatMap({
                tenant -> dbManager.tenantsDao?.save(tenant)})
            .map({
                val userCompanyRel = UserCompanyRel()
                userCompanyRel.userName = userId
                userCompanyRel.tenantId= tenantId
                userCompanyRel
            })
            .flatMap({
                item -> dbManager.userCompanyRelDao?.save(item)
            })
            .subscribe()
    }

    fun addUserToCompany(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            val request = JsonUtils.toPojo<MultiposRequest<String>>(message.body().toString())
            dbManager
                    .companyDao
                    ?.findById(request.data!!)
                    ?.doOnNext({ company ->/**company.ownersId.add(request.userId!!)*/ })
                    ?.flatMap({ company -> dbManager.companyDao?.save(company) })
                    ?.subscribe({ result ->
                        message.reply(MultiPosResponse<Any>(null, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()))
                    }, { error ->

                    })
        }

    }

    fun trashCompany(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            val request = JsonUtils.toPojo<MultiposRequest<String>>(message.body().toString())
            dbManager
                    .companyDao
                    ?.findById(request.data!!)
                    ?.flatMap({ item: Company ->
                        dbManager.companyDao?.trash(item)
                    })?.flatMap({ company ->
                        dbManager.tenantsDao?.getTenantByCompanyId(request.data!!)
                    })?.flatMap({tenant ->
                        dbManager.tenantsDao?.trash(tenant)
                    })?.flatMap({ tenants ->
                        dbManager.userCompanyRelDao?.trashByTenantId(tenantId = tenants.id!!, userId = request.userId!!)
                    })?.subscribe({ list ->
                        message.reply(MultiPosResponse<Any>(null, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    }, { error ->
                        when (error) {
                            is WriteDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                            is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                            is NotExistsException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.NOT_FOUND.code()).toJson())
                        }
                    })
        }
    }

    fun updateCompany(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            val request = JsonUtils.toPojo<MultiposRequest<Company>>(message.body().toString())
            dbManager
                    .companyDao
                    ?.update(request.data!!)
                    ?.subscribe({result ->
                        message.reply(MultiPosResponse<Any>(null, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()))
                    }, {error ->
                        when (error) {
                            is WriteDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()))
                            is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()))
                        }
                    })
        }

    }

    fun getCompaniesByUserId(message: Message<String>) {
        if (message.body() != null) {
            val jsonObject = JsonObject(message.body())
            val tenantId = jsonObject.getString("tenantId")
            val dbManager = getDbManagerByTenantId(tenantId = tenantId)
            val request = JsonUtils.toPojo<MultiposRequest<String>>(message.body().toString())
            dbManager
                    .companyDao
                    ?.getCompanyByMail(request.data!!)
                    ?.subscribe({ companies ->
                        message.reply(MultiPosResponse(companies, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                    }, { error ->
                        when(error) {
                            is ReadDbFailedException -> {
                                message.reply(MultiPosResponse<Any>(null,
                                        ErrorMessages.EMAIL_IS_EMPTY.value(), StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                            }
                            is Exception -> {
                                message.reply(MultiPosResponse<Any>(null,
                                        ErrorMessages.EMAIL_IS_EMPTY.value(), StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                            }
                        }
                    })
        }
    }

}