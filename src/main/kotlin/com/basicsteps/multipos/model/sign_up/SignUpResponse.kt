package com.basicsteps.multipos.model.sign_up

import com.google.gson.annotations.SerializedName

data class AccessCodeResponse(@SerializedName("access_code") val accesCode: Int)
data class EmailCheckResponse(@SerializedName("email_unique") val emailUnique: Boolean)