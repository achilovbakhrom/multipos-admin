package com.basicsteps.multipos.routing.handler

import com.basicsteps.multipos.config.CommonConstants
import com.basicsteps.multipos.model.ErrorMessages
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.Vertx
import io.vertx.ext.web.RoutingContext


class TenantSwitcherHandler(val vertx: Vertx) {
    fun switchTenant(routingContext: RoutingContext) {
        val tenantId: String? = routingContext.request().headers().get(CommonConstants.HEADER_TENANT)
        if (tenantId.isNullOrEmpty()) {
            routingContext.response().setStatusCode(HttpResponseStatus.UNAUTHORIZED.code()).end(ErrorMessages.CANT_FIND_X_TENANT_ID_HEADER.value())
            return
        } else {
            routingContext.next()
        }
    }
}