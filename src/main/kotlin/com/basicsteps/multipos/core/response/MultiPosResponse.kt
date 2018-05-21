package com.basicsteps.multipos.core.response

import com.basicsteps.multipos.utils.JsonUtils
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import javax.annotation.Nullable


class MultiPosResponse<T> constructor(@Nullable @SerializedName("data") var data: T? = null,
                                      @SerializedName("message") var message: String? = null,
                                      @SerializedName("status") var status: String? = null,
                                      @SerializedName("code") var code: Int? = null) : Serializable {
    constructor() : this(null, null, null, null)
    fun toJson() : String = JsonUtils.toJson(this)
    companion object {
        val OK = "OK"
    }
}