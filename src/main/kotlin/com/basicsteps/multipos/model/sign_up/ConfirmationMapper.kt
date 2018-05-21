package com.basicsteps.multipos.model.sign_up

import com.google.gson.annotations.SerializedName

data class ConfirmationMapper(@SerializedName("mail") val mail: String, @SerializedName("access_code") val accessCode: Int)
