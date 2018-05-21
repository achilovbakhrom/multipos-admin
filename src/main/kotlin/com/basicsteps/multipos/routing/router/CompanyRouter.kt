package com.basicsteps.multipos.routing.router

import com.basicsteps.multipos.core.router.BaseRouter
import com.basicsteps.multipos.core.router.RouteBuilder
import com.basicsteps.multipos.event_bus_channels.CompanyHandlerChannel
import com.basicsteps.multipos.model.Company
import com.basicsteps.multipos.model.entities.PaymentType
import com.basicsteps.multipos.utils.RoutingUtils
import io.vertx.core.Vertx
import io.vertx.ext.web.RoutingContext

class CompanyRouter (vertx: Vertx) : BaseRouter(vertx) {

    fun getCompaniesByUsername(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, CompanyHandlerChannel.GET_COMPANIES_BY_USERNAME.value())
    }

    fun createCompany(routingContext: RoutingContext) {
        val request = RoutingUtils.requestFromBody<Company>(routingContext)
        RoutingUtils.route<PaymentType>(vertx, routingContext, CompanyHandlerChannel.COMPANY_CREATE.value(), request.toJson())
    }

    fun updateCompany(routingContext: RoutingContext) {
        val request = RoutingUtils.requestFromBody<Company>(routingContext)
        RoutingUtils.route<PaymentType>(vertx, routingContext, CompanyHandlerChannel.COMPANY_UPDATE.value(), request.toJson())
    }

    fun getCompanyById(routingContext: RoutingContext) {
        val request = RoutingUtils.requestFromPathParams<String>(routingContext, "company_id")
        RoutingUtils.route<PaymentType>(vertx, routingContext, CompanyHandlerChannel.COMPANY_GET.value(), request.toJson())
    }
    fun deleteCompany(routingContext: RoutingContext) {
        val request = RoutingUtils.requestFromPathParams<String>(routingContext, "company_id")
        RoutingUtils.route<PaymentType>(vertx, routingContext, CompanyHandlerChannel.COMPANY_DELETE.value(), request.toJson())
    }

}