package com.basicsteps.multipos.model


enum class Scope(val scope: String) {

    SIMPLE_SCOPE("realm:simple_scope"),
    USER_SCOPE("realm:user_scope"),
    DEVELOPER_SCOPE("realm:developer_scope"),
    ADMINISTRATOR_SCOPE("realm:admin_scope");

    fun value() = scope
}