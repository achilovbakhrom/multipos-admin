package com.basicsteps.multipos.config



object FormatConstants {
    val dateFormat = "dd-MM-yyyy"
    val timeFormat = "mm:ss"
    val dateTimeFormat = "dd-MM-yyyy mm:ss"
}

object CommonConstants {
    val HEADER_TENANT = "X-TENANT-ID"
    val ANONYMOUS = "anonymous"
    val SWITCH_TENANT = "SWITCH_TENANT"
}

fun imageURL(imageName: String) : String {
    return "http://localhost:9090/avatars/$imageName"
}

