package com.basicsteps.multipos.config

enum class Roles(val role: String) {
    USER("user"),
    REGISTERED_USER("registered_user"),
    SUPER_USER("super_user"),
    MP_ADMIN("mp_admin");

    fun value() : String = role
}