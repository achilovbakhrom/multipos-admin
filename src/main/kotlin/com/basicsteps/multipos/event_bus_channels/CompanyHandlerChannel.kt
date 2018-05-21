package com.basicsteps.multipos.event_bus_channels

enum class CompanyHandlerChannel(val channel: String) {
    GET_COMPANIES_BY_USERNAME("GET_COMPANIES_BY_USERNAME"),
    COMPANY_CREATE("COMPANY_CREATE"),
    COMPANY_UPDATE("COMPANY_UPDATE"),
    COMPANY_GET("COMPANY_GET"),
    COMPANY_DELETE("COMPANY_DELETE");

    fun value() : String  = channel
}