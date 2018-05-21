package com.basicsteps.multipos.core.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ListObject(@SerializedName("total_name") var totalCount: Int, @SerializedName("objects") var objects: List<Any>) : Serializable