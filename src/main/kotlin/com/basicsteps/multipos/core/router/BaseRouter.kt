package com.basicsteps.multipos.core.router

import io.vertx.core.Vertx
import io.vertx.ext.web.RoutingContext

/**
 *  Base CRUD Router
 */
abstract class BaseRouter(val vertx: Vertx) {

    fun routeSave(routingContext: RoutingContext, channelName: String) {
        RouteBuilder().post().handle(vertx, routingContext, channelName)
    }

    fun routeFind(routingContext: RoutingContext, channelName: String) {
        RouteBuilder().get().handle(vertx, routingContext, channelName)
    }

    fun routeUpdate(routingContext: RoutingContext, channelName: String) {
        RouteBuilder().put().handle(vertx, routingContext, channelName)
    }

    fun routeTrash(routingContext: RoutingContext, channelName: String) {
        RouteBuilder().delete().handle(vertx, routingContext, channelName)
    }

}