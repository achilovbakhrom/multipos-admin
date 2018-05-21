package com.basicsteps.multipos.event_bus_channels

enum class SignInHandlerChannel(val channel: String) {
    SIGN_IN("SIGN_IN"),
    VERIFICATION("VERIFICATION"),
    REFRESH_ACCESS_TOKEN("REFRESH_ACCESS_TOKEN"),
    ADD_COMPANY("ADD_COMPANY"),
    GET_COMPANIES("GET_COMPANIES"),
    UPDATE_COMPANY("UPDATE_COMPANY"),
    REMOVE_COMPANY("REMOVE_COMPANY"),
    LOGOUT("LOGOUT");

    fun value() : String  = channel
}