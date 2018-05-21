package com.basicsteps.multipos.core.router

import com.basicsteps.multipos.config.CommonConstants
import com.basicsteps.multipos.core.putBrowserHeaders
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.model.StatusMessages
import io.vertx.core.AsyncResult
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.oauth2.impl.OAuth2TokenImpl
import io.vertx.ext.web.RoutingContext
import javax.ws.rs.InternalServerErrorException

class RouteBuilder {

    var kPost: PostRoute? = null
    var kGet: GetRoute? = null
    var kPut: PutRoute? = null
    var kDelete: DeleteRoute? = null

    fun post() : PostRoute {
        kPost = PostRoute()
        return kPost!!
    }

    fun get() : GetRoute {
        kGet = GetRoute()
        return kGet!!
    }

    fun put() : PutRoute {
        kPut = PutRoute()
        return kPut!!
    }

    fun delete() : DeleteRoute {
        kDelete = DeleteRoute()
        return kDelete!!
    }

}


class PostRoute {

    fun handle(vertx: Vertx, routingContext: RoutingContext, channelName: String) {

        val jsonObject = JsonObject()
        if (routingContext.user() != null)
            jsonObject.put("userId", (routingContext.user() as OAuth2TokenImpl).accessToken().getString("email"))

        val tenantId: String? = routingContext.request().headers().get(CommonConstants.HEADER_TENANT)
        if (tenantId != null) {
            jsonObject.put("tenantId", tenantId)
        }


        val hasBody = routingContext.bodyAsString != null && !routingContext.bodyAsString.isEmpty()

        if (hasBody) {
            val dataJsonObject: JsonObject
            try {
                dataJsonObject = JsonObject(routingContext.bodyAsString)
            } catch (e: Exception) {
                routingContext
                        .response()
                        .setStatusCode(400)
                        .end(MultiPosResponse(null, "Send correct json", StatusMessages.ERROR.value(), 400).toJson())
                return
            }

            if (jsonObject.getJsonArray("array") != null) {
                val array = jsonObject.getJsonArray("array")
                jsonObject.put("data", array)
            } else {
                jsonObject.put("data", dataJsonObject)
            }
        }

        if (!hasBody && jsonObject.isEmpty) {
            routingContext
                    .response()
                    .setStatusCode(400)
                    .end(MultiPosResponse(null, "Nothing has received!", StatusMessages.ERROR.value(), 400).toJson())
            return
        }


        vertx.eventBus().send(channelName, jsonObject.toString(), { reply: AsyncResult<Message<String>> ->
            if (reply.succeeded()) {
                val body = reply.result().body()
                val jsonObject = JsonObject(body)
                val statusCode = jsonObject.getInteger("code")
                routingContext
                        .response()
                        .putBrowserHeaders()
                        .setStatusCode(statusCode)
                        .end(body)
            } else{
                routingContext.fail(InternalServerErrorException("Bus problems"))
            }
        })
    }
}


class GetRoute {

    fun handle(vertx: Vertx, routingContext: RoutingContext, channelName: String) {

        val jsonObject = JsonObject()
        if (routingContext.user() != null)
            jsonObject.put("userId", (routingContext.user() as? OAuth2TokenImpl)?.accessToken()?.getString("email"))

        val tenantId: String? = routingContext.request().headers().get(CommonConstants.HEADER_TENANT)
        if (tenantId != null) {
            jsonObject.put("tenantId", tenantId)
        }

        val paramJson = JsonObject()
        if (!routingContext.pathParams().isEmpty()) {
            routingContext.pathParams().forEach({ paramJson.put(it.key, it.value) })
        }

        if (!routingContext.queryParams().isEmpty) {
            routingContext.queryParams().forEach({ paramJson.put(it.key, it.value) })
        }

        if (!paramJson.isEmpty) {
            jsonObject.put("params", paramJson)
        }

        vertx.eventBus().send(channelName, jsonObject.toString(), { reply: AsyncResult<Message<String>> ->
            if (reply.succeeded()) {
                val body = reply.result().body()
                val jsonObject = JsonObject(body)
                val statusCode = jsonObject.getInteger("code")
                routingContext
                        .response()
                        .putBrowserHeaders()
                        .setStatusCode(statusCode)
                        .end(body)
            } else{
                routingContext.fail(InternalServerErrorException("Bus problems"))
            }
        })


    }


}

class PutRoute {

    fun handle(vertx: Vertx, routingContext: RoutingContext, channelName: String) {
        val jsonObject = JsonObject()
        if (routingContext.user() != null)
            jsonObject.put("userId", (routingContext.user() as OAuth2TokenImpl).accessToken().getString("email"))

        val tenantId: String? = routingContext.request().headers().get(CommonConstants.HEADER_TENANT)
        if (tenantId != null) {
            jsonObject.put("tenantId", tenantId)
        }

        val hasBody = routingContext.bodyAsString != null && !routingContext.bodyAsString.isEmpty()

        if (hasBody) {
            val dataJsonObject: JsonObject
            try {
                dataJsonObject = JsonObject(routingContext.bodyAsString)
            } catch (e: Exception) {
                routingContext
                        .response()
                        .setStatusCode(400)
                        .end(MultiPosResponse(null, "Send correct json", StatusMessages.ERROR.value(), 400).toJson())
                return
            }
            if (jsonObject.getJsonArray("array") != null) {
                val array = jsonObject.getJsonArray("array")
                jsonObject.put("data", array.toString())
            } else {
                jsonObject.put("data", dataJsonObject)
            }
        }

        if (!hasBody && jsonObject.isEmpty) {
            routingContext
                    .response()
                    .setStatusCode(400)
                    .end(MultiPosResponse(null, "Nothing has received!", StatusMessages.ERROR.value(), 400).toJson())
            return
        }

        vertx.eventBus().send(channelName, jsonObject.toString(), { reply: AsyncResult<Message<String>> ->
            if (reply.succeeded()) {
                val body = reply.result().body()
                val jsonObject = JsonObject(body)
                val statusCode = jsonObject.getInteger("code")
                routingContext
                        .response()
                        .putBrowserHeaders()
                        .setStatusCode(statusCode)
                        .end(body)
            } else{
                routingContext.fail(InternalServerErrorException("Bus problems"))
            }
        })
    }

}

class DeleteRoute {

    fun handle(vertx: Vertx, routingContext: RoutingContext, channelName: String) {

        val jsonObject = JsonObject()
        if (routingContext.user() != null)
            jsonObject.put("userId", (routingContext.user() as OAuth2TokenImpl).accessToken().getString("email"))

        val tenantId: String? = routingContext.request().headers().get(CommonConstants.HEADER_TENANT)
        if (tenantId != null) {
            jsonObject.put("tenantId", tenantId)
        }

        val paramJson = JsonObject()
        if (!routingContext.pathParams().isEmpty()) {
            routingContext.pathParams().forEach({ paramJson.put(it.key, it.value) })
        }

        if (!routingContext.queryParams().isEmpty) {
            routingContext.queryParams().forEach({ paramJson.put(it.key, it.value) })
        }

        if (!paramJson.isEmpty) {
            jsonObject.put("params", paramJson)
        }

        val hasBody = routingContext.bodyAsString != null && !routingContext.bodyAsString.isEmpty()

        if (hasBody) {
            val dataJsonObject: JsonObject
            try {
                dataJsonObject = JsonObject(routingContext.bodyAsString)
            } catch (e: Exception) {
                routingContext
                        .response()
                        .setStatusCode(400)
                        .end(MultiPosResponse(null, "Send correct json", StatusMessages.ERROR.value(), 400).toJson())
                return
            }

            if (jsonObject.getJsonArray("array") != null) {
                val array = jsonObject.getJsonArray("array")
                jsonObject.put("data", array)
            } else {
                jsonObject.put("data", dataJsonObject)
            }
        }

        if (hasBody && jsonObject.isEmpty) {
            routingContext
                    .response()
                    .setStatusCode(400)
                    .end(MultiPosResponse(null, "Nothing has received!", StatusMessages.ERROR.value(), 400).toJson())
            return
        }

        vertx.eventBus().send(channelName, jsonObject.toString(), { reply: AsyncResult<Message<String>> ->
            if (reply.succeeded()) {
                val body = reply.result().body()
                val jsonObject = JsonObject(body)
                val statusCode = jsonObject.getInteger("code")
                routingContext
                        .response()
                        .putBrowserHeaders()
                        .setStatusCode(statusCode)
                        .end(body)
            } else{
                routingContext.fail(InternalServerErrorException("Bus problems"))
            }
        })

    }
}
