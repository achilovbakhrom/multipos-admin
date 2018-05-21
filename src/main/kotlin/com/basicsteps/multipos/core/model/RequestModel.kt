package com.basicsteps.multipos.core.model

import com.google.gson.annotations.SerializedName

@JvmSuppressWildcards(suppress = true)
data class RequestModel(@SerializedName("id") var id: String = "",
                        @SerializedName("page") var page: Int = 0,
                        @SerializedName("page_size") var pageSize: Int = 0)