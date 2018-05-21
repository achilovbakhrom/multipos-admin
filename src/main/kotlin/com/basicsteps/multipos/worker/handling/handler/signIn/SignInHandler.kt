package com.basicsteps.multipos.worker.handling.handler.signIn

import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.model.ErrorMessages
import com.basicsteps.multipos.model.StatusMessages
import com.basicsteps.multipos.model.sign_in.SignInMapper
import com.basicsteps.multipos.model.sign_in.SignInResponseMapper
import com.basicsteps.multipos.model.sign_in.VerificationMapper
import com.basicsteps.multipos.utils.JsonUtils
import com.basicsteps.multipos.utils.KeycloakConfig
import com.basicsteps.multipos.utils.SystemConfig
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.oauth2.AccessToken
import io.vertx.ext.auth.oauth2.OAuth2Auth
import io.vertx.ext.auth.oauth2.OAuth2ClientOptions
import io.vertx.ext.auth.oauth2.OAuth2FlowType

class SignInHandler(vertx: Vertx): BaseCRUDHandler(vertx) {

    fun verification(message: Message<String>) {
        val username = message.body()
        val dbManager = getDbManagerByTenantId(tenantId = "")
        val users = dbManager.keycloak?.realm(KeycloakConfig.model.realm)?.users()?.search(username)

        if (users != null && !users.isEmpty()) {
            val foundUser = users.get(0)
            val tenantId = foundUser.attributes.get("X-TENANT-ID")?.get(0).toString()
            val name = foundUser.firstName + " " + foundUser.lastName
            message.reply(MultiPosResponse(VerificationMapper(username, name, tenantId), null, "Success", HttpResponseStatus.OK.code()).toJson())
        } else
            message.reply(MultiPosResponse<Any>(null, "$username not found", "Error", HttpResponseStatus.UNAUTHORIZED.code()).toJson())

    }

    fun signInHandler(message: Message<String>) {
        val jsonObject = JsonObject(message.body())
        val signInMapper = JsonUtils.toPojo<SignInMapper>(jsonObject.getJsonObject("data").toString())
        val tokenConfig = JsonObject()
                .put("username", signInMapper.username)
                .put("password", signInMapper.password)

        val credentials = OAuth2ClientOptions()
                .setClientID(signInMapper.clientId)
                .setClientSecret(signInMapper.clientSecret)
                .setTokenPath(KeycloakConfig.model.accessTokenEndpoint)
                .setSite(SystemConfig.model.baseURL())

        // Callbacks
        // Save the access token
        val temp = OAuth2Auth.create(vertx, OAuth2FlowType.PASSWORD, credentials)

        temp.authenticate(tokenConfig, {res ->
            if (res.failed()) {
                System.err.println("Access Token Error: " + res.cause().message)
                message.reply(MultiPosResponse<Any>(null, res.cause().localizedMessage, StatusMessages.ERROR.value(), HttpResponseStatus.BAD_REQUEST.code()).toJson())
            } else {

                // Get the access token object (the authorization code is given from the previous step).
                val token = res.result() as AccessToken
                val data = JsonUtils.toPojo<SignInResponseMapper>(JsonUtils.toJson(token.principal().map))
                message.reply(MultiPosResponse(data, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
            }
        })
    }

    fun logout(message: Message<String>) {
        val username = message.body()
        val dbManager = getDbManagerByTenantId(tenantId = "")
        val users = dbManager.keycloak?.realm(KeycloakConfig.model.realm)?.users()?.search(username)
        if (users?.isEmpty()!!) {
            message.reply(MultiPosResponse(null, ErrorMessages.EMAIL_NOT_EXIST.value(), StatusMessages.ERROR.value(), HttpResponseStatus.NOT_FOUND.code()).toJson())
        } else {
            dbManager.keycloak?.realm(KeycloakConfig.model.realm)?.users()?.get(users[0].id)?.logout()
            message.reply(MultiPosResponse(null, null, StatusMessages.SUCCESS.value(), HttpResponseStatus.OK.code()).toJson())
        }
    }

    fun refreshAccessToken(message: Message<String>) {
        //TODO refresh token

    }

}