package com.basicsteps.multipos.utils

import com.basicsteps.multipos.core.response.MultiposRequest
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import io.vertx.core.json.JsonObject

/**
 * Created at 20.01.2018
 * @author Achilov Bakhrom
 *
 * Singleton Helper for converting POJO to json_string
 * and backward
 */
object JsonUtils {

    /**
     * json_string to POJO method
     */
    inline fun <reified T> toPojo(json: String): T {
        val turnType = object : TypeToken<T>() {}.type
        return Gson().fromJson(json, turnType)
    }

    /**
     * POJO to json_string method
     */
    fun <T> toJson(t: T): String {
        return Gson().toJson(t)
    }

    /**
     * POJO to JsonObject method
     */
    fun <T> toJsonObject(pojo: T) : JsonObject {
        return JsonObject(toJson(pojo))
    }

}