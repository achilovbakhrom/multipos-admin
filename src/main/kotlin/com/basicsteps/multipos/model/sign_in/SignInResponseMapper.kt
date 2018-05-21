package com.basicsteps.multipos.model.sign_in

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SignInResponseMapper(@SerializedName("access_token") var accessToken: String,
                                @SerializedName("expires_in") var expiresIn: Int,
                                @SerializedName("refresh_expires_in") var refreshExpiresIn: Int,
                                @SerializedName("refresh_token") var refreshToken: String,
                                @SerializedName("token_type") var tokenType: String,
                                @SerializedName("not_before_policy") var notBeforePolicy: String,
                                @SerializedName("session_state") var sessionState: String,
                                @SerializedName("expires_at") var expiresAt: Long,
                                @SerializedName("username") var username: String,
                                @SerializedName("mail") var mail: String) : Serializable