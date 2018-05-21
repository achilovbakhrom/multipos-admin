package com.basicsteps.multipos.worker.handling.handler.signUp

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import com.basicsteps.multipos.core.isEmail
import com.basicsteps.multipos.core.model.exceptions.*
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.model.ErrorMessages
import com.basicsteps.multipos.model.StatusMessages
import com.basicsteps.multipos.model.sign_up.AccessCodeResponse
import com.basicsteps.multipos.model.sign_up.EmailCheckResponse
import com.basicsteps.multipos.model.sign_up.SignUpMapper
import com.basicsteps.multipos.utils.GenratorUtils
import com.basicsteps.multipos.utils.JsonUtils
import com.basicsteps.multipos.utils.sendEmail
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject

class SignUpHandler(vertx: Vertx) : BaseCRUDHandler(vertx) {

    /**
     * SignUp handler
     * Saves SignUpMapper received from user
     */
    fun signUp(message: Message<String>) {
        val jsonObject = JsonObject(message.body())
        val signUpMapper = JsonUtils.toPojo<SignUpMapper>(jsonObject.getJsonObject("data").toString())
        if (!signUpMapper.mail.isEmpty()) {
            if (!signUpMapper.mail.isEmail()) {
                message.reply(MultiPosResponse<Any>(null,
                        ErrorMessages.EMAIL_IS_NOT_VALID.value(), StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
                return
            }
        } else {
            message.reply(MultiPosResponse<Any>(null,
                    ErrorMessages.EMAIL_IS_EMPTY.value(), StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
            return
        }

        if (signUpMapper.password.isEmpty()) {
            message.reply(MultiPosResponse<Any>(null,
                    ErrorMessages.PASSWORD_IS_NOT_VALID.value(), StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
            return
        }

        val accessCode = GenratorUtils.generateRandomInt(100000, 999999)
        signUpMapper.accessCode = accessCode
        val dbManager = getDbManagerByTenantId(tenantId = "")
        dbManager.signUpDao?.emailUnique(signUpMapper.mail)?.subscribe({result ->
            if (result) {
                sendEmail(signUpMapper.mail, "Sending access code, MULTIPOS SYSTEMS", "Access Code is: <b> $accessCode </b>", {
                    dbManager.signUpDao
                            ?.save(signUpMapper)
                            ?.subscribe({
                                message.reply(MultiPosResponse<Any>(it, null,
                                        StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())

                            }, {error ->
                                error.printStackTrace()
                                when(error) {
                                    is DataStoreException -> message.reply(MultiPosResponse<Any>(null, error.message,
                                            StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                                    is WriteDbFailedException -> message.reply(MultiPosResponse<Any>(null,
                                            error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                                }
                            })
                }, {
                    message
                            .reply(MultiPosResponse<Any>(null,
                                    ErrorMessages.EMAIL_NOT_EXIST.value(), StatusMessages.ERROR.value(), HttpResponseStatus.NOT_FOUND.code()).toJson())
                })
            } else {
                message
                        .reply(MultiPosResponse<Any>(null,
                                ErrorMessages.EMAIL_IS_NOT_UNIQUE.value(), StatusMessages.ERROR.value(), HttpResponseStatus.CONFLICT.code()).toJson())
            }
        }, {error ->
            message.reply(MultiPosResponse<Any>(null,
                    error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
        })

    }

    /**
     * GetAccessCode
     * returns corresponding access code
     * requested by user with his mail
     */
    fun getAccessCode(message: Message<String>) {
        val jsonObject = JsonObject(message.body())
        val email: String
        if (jsonObject.getJsonObject("params") != null) {
            val params = jsonObject.getJsonObject("params")
            if (params.getString("email") == null) {
                message.reply(MultiPosResponse(null, "email is empty", StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
                return
            } else {
                email = params.getString("email")
            }

        } else {
            message.reply(MultiPosResponse(null, "email is empty", StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
            return
        }
        val dbManager = getDbManagerByTenantId(tenantId = "")
        dbManager.signUpDao
                ?.getSignUpMapperByMail(email)
                ?.subscribe({ item ->
                    message.reply(MultiPosResponse(AccessCodeResponse(item.accessCode), null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                }, { error ->

                    when(error) {
                        is DataStoreException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is ReadDbFailedException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is NotExistsException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.NOT_FOUND.code()).toJson())
                        is FieldConflictsException -> message.reply(MultiPosResponse(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.CONFLICT.code()).toJson())
                    }
                })
    }

    /**
     * IsEmailExists
     * returns existence of email in db
     */
    fun isEmailExists(message: Message<String>) {
        val jsonObject = JsonObject(message.body())
        val email: String
        if (jsonObject.getJsonObject("params") != null) {
            val params = jsonObject.getJsonObject("params")
            if (params.getString("email") == null) {
                message.reply(MultiPosResponse(null, "email is empty", StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
                return
            } else {
                email = params.getString("email")
            }

        } else {
            message.reply(MultiPosResponse(null, "email is empty", StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
            return
        }

        if (email.isNullOrEmpty()) {
            message.reply(MultiPosResponse<Any>(null,
                    ErrorMessages.EMAIL_IS_EMPTY.value(), StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
            return
        }
        if (!email.isEmail()) {
            message.reply(MultiPosResponse<Any>(null,
                    ErrorMessages.EMAIL_IS_NOT_VALID.value(), StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
            return
        }
        val dbManager = getDbManagerByTenantId(tenantId = "")
        dbManager.signUpDao
                ?.emailUnique(email)
                ?.subscribe({unique ->
                    message.reply(MultiPosResponse(EmailCheckResponse(unique), null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                }, {error ->
                    when(error) {
                        is DataStoreException -> message.reply(MultiPosResponse<Any>(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                        is ReadDbFailedException -> message.reply(MultiPosResponse<Any>(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    }
                })
    }

    /**
     * HandleConfirmation
     * confirms signUpMapper object data
     * by sending access code from user
     */
    fun handleConfirmation(message: Message<String>) {
        val jsonObject = JsonObject(message.body())
        val email: String
        val accessCode: String
        if (jsonObject.getJsonObject("params") != null) {
            val params = jsonObject.getJsonObject("params")
            if (params.getString("email") == null) {
                message.reply(MultiPosResponse(null, "email is empty", StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
                return
            } else {
                email = params.getString("email")
            }

            if (params.getString("access_code") == null) {
                message.reply(MultiPosResponse(null, "access_code is empty", StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
                return
            } else {
                accessCode = params.getString("access_code")
            }
        } else {
            message.reply(MultiPosResponse(null, "email and access_code are empty", StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
            return
        }
        val dbManager = getDbManagerByTenantId(tenantId = "")
        dbManager.signUpDao
            ?.accessCode(email, accessCode)
            ?.subscribe({created ->
                if (created) {
                    message.reply(MultiPosResponse<Any>(created, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
                } else {
                    message.reply(MultiPosResponse<Any>(created, ErrorMessages.WRONG_ACCESS_CODE.value(), StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
                }
            }, {error ->
                when(error) {
                    is NotExistsException -> message.reply(MultiPosResponse<Any>(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.NOT_FOUND.code()).toJson())
                    is FieldConflictsException -> message.reply(MultiPosResponse<Any>(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.CONFLICT.code()).toJson())
                    is ReadDbFailedException -> message.reply(MultiPosResponse<Any>(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    is DataStoreException -> message.reply(MultiPosResponse<Any>(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                    is DeleteDbFailedException -> message.reply(MultiPosResponse<Any>(null, error.message, StatusMessages.ERROR.value(), HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).toJson())
                }
            })
    }
}