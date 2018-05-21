package com.basicsteps.multipos.event_bus_channels

enum class SignUpHandlerChannel(val channel: String) {
    SIGN_UP("SIGN_UP"),
    CONFIRM_ACCESS_CODE("CONFIRM_ACCESS_CODE"),
    IS_EMAIL_UNIQUE("IS_EMAIL_UNIQUE"),
    GET_ACCESS_CODE("GET_ACCESS_CODE"),
    ADD_COMPANY("ADD_COMPANY"),
    UPLOAD_AVATAR("UPLOAD_AVATAR");


    fun value() : String  = channel
}