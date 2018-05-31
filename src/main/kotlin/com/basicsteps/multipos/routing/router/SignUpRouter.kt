package com.basicsteps.multipos.routing.router

import com.basicsteps.multipos.core.router.BaseRouter
import com.basicsteps.multipos.core.router.RouteBuilder
import com.basicsteps.multipos.event_bus_channels.SignUpHandlerChannel
import com.basicsteps.multipos.worker.handling.handler.common.UploadHandler
import io.vertx.core.Vertx
import io.vertx.ext.web.RoutingContext

class SignUpRouter (vertx: Vertx) : BaseRouter(vertx) {

    fun confirmAccessCode(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, SignUpHandlerChannel.CONFIRM_ACCESS_CODE.value())
    }

    fun isEmailExists(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, SignUpHandlerChannel.IS_EMAIL_UNIQUE.value())
    }

    fun getAccessCode(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, SignUpHandlerChannel.GET_ACCESS_CODE.value())
    }

    fun uploadAvatar(routingContext: RoutingContext) {
        UploadHandler.uploadAvatar(vertx, routingContext)
    }

    fun signUp(routingContext: RoutingContext) {
        RouteBuilder().post().handle(vertx, routingContext, SignUpHandlerChannel.SIGN_UP.value())
    }


}