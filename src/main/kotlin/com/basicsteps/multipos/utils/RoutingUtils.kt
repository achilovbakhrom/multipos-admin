package com.basicsteps.multipos.utils

import com.basicsteps.multipos.core.putBrowserHeaders
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.core.response.MultiposRequest
import com.google.gson.JsonObject
import io.vertx.core.AsyncResult
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.ext.auth.oauth2.impl.OAuth2TokenImpl
import io.vertx.ext.web.RoutingContext
import javax.ws.rs.InternalServerErrorException

object RoutingUtils {

    inline fun <reified T> route(vertx: Vertx, routingContext: RoutingContext, channelName: String, body: String? = null) {
        vertx.eventBus().send(channelName, body, { reply: AsyncResult<Message<String>> ->
            if (reply.succeeded()) {
                val result = JsonUtils.toPojo<MultiPosResponse<T>>(reply.result().body())
                routingContext
                        .response()
                        .putBrowserHeaders()
                        .setStatusCode(result.code!!)
                        .end(result.toJson())
            } else{
                routingContext
                        .fail(InternalServerErrorException(reply.cause()))

//                routingContext
//                        .response()
//                        .putBrowserHeaders()
//                        .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
//                        .end(reply.cause().toString())
            }
        })
    }

    inline fun <reified T> requestFromPathParams(routingContext: RoutingContext, pathParamName: String): MultiposRequest<T> {
        val request = MultiposRequest<T>()
        request.userId = (routingContext.user() as OAuth2TokenImpl).accessToken().getString("email")
        request.data = routingContext.pathParam(pathParamName) as? T
        return request
    }

    inline fun <reified T> requestFromQueryParams(routingContext: RoutingContext, queryParamName: String): MultiposRequest<T> {
        val request = MultiposRequest<T>()
        request.userId = (routingContext.user() as? OAuth2TokenImpl)?.accessToken()?.getString("email")
        request.data = routingContext.queryParam(queryParamName)[0] as? T
        return request
    }

    inline fun <reified T> requestFromBody(routingContext: RoutingContext): MultiposRequest<T> {
        val request = MultiposRequest<T>()
        request.userId = (routingContext.user() as OAuth2TokenImpl).accessToken().getString("email")
        request.data = JsonUtils.toPojo<T>(routingContext.body.toString())
        return request
    }

    fun requestFromQueryParams(routingContext: RoutingContext, queryParamNames: List<String>): MultiposRequest<JsonObject> {
        val request = MultiposRequest<JsonObject>()
        request.userId = (routingContext.user() as? OAuth2TokenImpl)?.accessToken()?.getString("email")
        val jsonObject = JsonObject()
        for (query in queryParamNames) {
            jsonObject.addProperty(query, routingContext.queryParam(query).get(0))
        }
        request.data = jsonObject
        return request
    }


}