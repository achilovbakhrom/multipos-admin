package com.basicsteps.multipos.core.response

import com.basicsteps.multipos.utils.JsonUtils
import com.fasterxml.jackson.databind.util.ClassUtil.classOf
import com.google.gson.annotations.SerializedName
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@JvmSuppressWildcards
open class MultiposRequest<T> {

    @SerializedName("data")
    var data: T? = null

    @SerializedName("userId") var userId: String? = null

    fun toJson(): String {
        return JsonUtils.toJson(this)
    }

}