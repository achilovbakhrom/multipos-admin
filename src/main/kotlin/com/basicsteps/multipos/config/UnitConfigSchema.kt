package com.basicsteps.multipos.config

import com.google.gson.annotations.SerializedName

class KeycloakConfigSchema { @SerializedName("keycloak") var keycloak: Map<String, Any>? = null }
class ServerConfigSchema { @SerializedName("server") var server: Map<String, Any>? = null }
class CountryConfigSchema { @SerializedName("countries") var countries: Map<String, Map<String, Map<String, Any>>>? = null }
class UnitConfigSchema { @SerializedName("units") var units: Map<String, Map<String, Map<String, Any>>>? = null }
class CurrencyConfigSchema { @SerializedName("currencies") var currencies: Map<String, Map<String, Any>>? = null}
class DbConfigSchema { @SerializedName("db") var db:Map<String, Any>? = null }
