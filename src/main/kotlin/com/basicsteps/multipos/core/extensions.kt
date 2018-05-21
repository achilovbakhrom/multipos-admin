package com.basicsteps.multipos.core

import com.basicsteps.multipos.utils.ValidationUtils
import com.sun.org.apache.xpath.internal.operations.Bool
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServerResponse
import io.vertx.ext.web.handler.CorsHandler


fun HttpServerResponse.putBrowserHeaders() : HttpServerResponse {
    return this.putHeader("content-type", "application/json")
            .putHeader("Access-Control-Allow-Origin", "*")
            .putHeader("Access-Control-Allow-Methods","GET, POST, PUT, DELETE, OPTIONS")
//            .putHeader("Access-Control-Allow-Credentials", "true")
            .putHeader("Access-Control-Allow-Headers", "Content-type,Authorization,Accept,X-Access-Token,X-Key")
}

fun String.isPhoneNumber() : Boolean {
    return ValidationUtils.phoneNumber(this)
}

fun String.isEmail() : Boolean {
    return ValidationUtils.email(this)
}

fun Any?.isNull(callback: (isNull: Boolean) -> Unit) {
    callback(this == null)
}

fun CorsHandler.putCorsAccesses() : CorsHandler {
    this.allowedMethod(HttpMethod.GET)
            .allowedMethod(HttpMethod.DELETE)
            .allowedMethod(HttpMethod.POST)
            .allowedMethod(HttpMethod.PUT)
            .allowedMethod(HttpMethod.PATCH)
            .allowedHeader("Authorization")
            .allowedHeader("user-agent")
            .allowedHeader("Access-Control-Request-Method")
            .allowedHeader("Access-Control-Allow-Credentials")
            .allowedHeader("Access-Control-Allow-Origin")
            .allowedHeader("Access-Control-Allow-Headers")
            .allowedHeader("Content-Type")
            .allowedHeader("Content-Length")
            .allowedHeader("X-Requested-With")
            .allowedHeader("x-auth-token")
            .allowedHeader("Location")
            .exposedHeader("Location")
            .exposedHeader("Content-Type")
            .exposedHeader("Content-Length")
            .exposedHeader("ETag")
            .maxAgeSeconds(60)
    return this
}