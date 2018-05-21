package com.basicsteps.multipos.routing.handler


import com.basicsteps.multipos.model.ErrorMessages
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.ext.auth.oauth2.impl.OAuth2TokenImpl
import io.vertx.ext.web.RoutingContext

class CorrectAccessTokenHandler(val vertx: Vertx) {

    fun isAccessCodeCorrect(routingContext: RoutingContext) {
        if ((routingContext.user() as OAuth2TokenImpl).accessToken() == null) {
            routingContext.response().setStatusCode(HttpResponseStatus.UNAUTHORIZED.code()).end(ErrorMessages.WRONG_ACCESS_CODE.value())
        } else {
            routingContext.next()
        }
    }

}