package com.basicsteps.multipos.model.sign_in

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VerificationMapper(@SerializedName("username") val username: String,
                              @SerializedName("name") val name: String,
                              @SerializedName("tenant_id") val tenantId: String) : Serializable

