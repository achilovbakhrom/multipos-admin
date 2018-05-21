package com.basicsteps.multipos.model

import com.google.gson.annotations.SerializedName

data class Config
    constructor(@SerializedName("host") val host: String,
                @SerializedName("port") val port: Int,
                @SerializedName("mongo_uri") val mongoUri: String,
                @SerializedName("sign_up_db_name") val signUpDbName: String,
                @SerializedName("sign_in_db_name") val signInDbName: String,
                @SerializedName("keycloak_admin") val keycloakAdimn: String,
                @SerializedName("keycloak_admin_password") val keycloakAdminPassword: String)