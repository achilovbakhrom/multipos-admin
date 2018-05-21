package com.basicsteps.multipos.core

import com.basicsteps.multipos.utils.JsonUtils
import io.netty.handler.codec.http.HttpResponseStatus

data class BusResponse<T>(var status: Int, var message: String) {
    var map: HashMap<String, Any> = HashMap()
    var data: T? = null

    init {
        map.put("status", status)
        map.put("message", message)
    }

    fun toJson() : String {
        if (data != null)
            map.put("data", data!!)
        return JsonUtils.toJson(this.map)
    }

    fun putField(key: String, value: Any) {
        map.put(key, value)
    }

    companion object {
        fun okMsg() : String {
            return BusResponse<Any>(HttpResponseStatus.OK.code(), "Ok").toJson()
        }

        fun okMsg(key: String, value: Any) : String {
            val result = BusResponse<Any>(HttpResponseStatus.OK.code(), "Ok")
            result.putField(key, value)
            return result.toJson()
        }

        fun okMsg(keys: Array<String>, values: Array<String>) : String {
            val result = BusResponse<Any>(HttpResponseStatus.OK.code(), "Ok")
            for (i in 0..keys.size-1) {
                result.putField(keys[i], values[i])
            }
            return result.toJson()
        }
    }
}