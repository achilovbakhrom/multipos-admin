package com.basicsteps.multipos.model.sign_up

import com.google.gson.annotations.SerializedName

data class MailMapper(@SerializedName("mail") val mail: String) {
    constructor() : this("")
}