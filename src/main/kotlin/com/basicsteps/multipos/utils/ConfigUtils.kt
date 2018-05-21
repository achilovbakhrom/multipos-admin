package com.basicsteps.multipos.utils

import com.basicsteps.multipos.config.*
import com.basicsteps.multipos.model.Country
import com.basicsteps.multipos.model.entities.Currency
import com.basicsteps.multipos.model.entities.UnitCategoryEntity
import com.basicsteps.multipos.model.entities.UnitEntity
import io.reactivex.Observable
import io.vertx.core.Vertx
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

/**
 * Created at 20.01.2018
 *
 * @author Achilov Bakhrom
 *
 * Singleton Helper for creating per System data
 * from the resources
 */
object SystemConfig {

    lateinit var model: SystemConfigModel

    fun initSystemConfig(vertx: Vertx) : Observable<Boolean> {
        return Observable.create({event ->
            vertx.fileSystem().readFile("${System.getProperty("user.dir")}/src/main/resources/server-config.yaml", { handler ->
                if (handler.succeeded()) {
                    val yamlString = handler.result().toString()
                    val constructor = Constructor(ServerConfigSchema::class.java)
                    val yaml = Yaml(constructor)
                    val data = yaml.loadAs(yamlString, ServerConfigSchema::class.java)
                    model = SystemConfigModel(
                            (data.server as? Map<*, *>)?.get("host").toString(),
                            (data.server as? Map<*, *>)?.get("port").toString(),
                            (data.server as? Map<*, *>)?.get("resource_host").toString(),
                            (data.server as? Map<*, *>)?.get("resource_port").toString(),
                            ResourcePath(
                                    (data.server as? Map<*, *>)?.get("unit").toString(),
                                    (data.server as? Map<*, *>)?.get("currency").toString(),
                                    (data.server as? Map<*, *>)?.get("country").toString()
                            )
                    )
                    event.onNext(true)
                } else {
                    event.onError(Exception(handler.cause()))
                }
            })
        })
    }

    fun getCountryList(vertx: Vertx) : Observable<List<Country>> {
        return Observable.create({ event ->
            vertx.fileSystem().readFile(model?.countryPath(), { handler ->
                if (handler.succeeded()) {
                    val result = mutableListOf<Country>()
                    val yamlString = handler.result().toString()
                    val constructor = Constructor(CountryConfigSchema::class.java)
                    val yaml = Yaml(constructor)
                    val data = yaml.loadAs(yamlString, CountryConfigSchema::class.java)
                    data.countries?.keys?.forEach { key ->
                        val lang = (data.countries?.get(key) as? Map<*, *>)?.get("lang").toString()
                        val name = (data.countries?.get(key) as? Map<*, *>)?.get("name").toString()
                        val country = Country(lang, name)
                        result.add(country)
                    }
                    event.onNext(result)
                } else {
                    event.onError(Exception(handler.cause()))
                }
            })
        })
    }

    /**
     * Generating all units from file: resources/unit.yaml
     */
    fun getUnitCategories(vertx: Vertx) : Observable<List<UnitCategoryEntity>> {
        return Observable.create({ event ->
            vertx.fileSystem().readFile(model?.unitPath(), { handler ->
                if (handler.succeeded()) {
                    val result = mutableListOf<UnitCategoryEntity>()
                    val yamlString = handler.result().toString()
                    val constructor = Constructor(UnitConfigSchema::class.java)
                    val yaml = Yaml(constructor)
                    val data = yaml.loadAs(yamlString, UnitConfigSchema::class.java)
                    data.units?.keys?.forEach { key ->
                        val unitCategory = UnitCategoryEntity()
                        unitCategory.id = key
                        unitCategory.name = key
                        result.add(unitCategory)
                    }
                    event.onNext(result)
                } else {
                    event.onError(Exception(handler.cause()))
                }
            })
        })
    }

    /**
     *  Generating all units from file: resources/unit.yaml
     */
    fun getUnits(vertx: Vertx) : Observable<List<UnitEntity>> {
        return Observable.create({event ->
            vertx.fileSystem().readFile(model.unitPath(), { handler ->
                if (handler.succeeded()) {
                    val result = mutableListOf<UnitEntity>()
                    val yamlString = handler.result().toString()
                    val constructor = Constructor(UnitConfigSchema::class.java)
                    val yaml = Yaml(constructor)
                    val data = yaml.loadAs(yamlString, UnitConfigSchema::class.java)
                    data.units?.keys?.forEach { key ->
                        data.units?.get(key)?.keys?.forEach { innerKey ->
                            val unit = UnitEntity()
                            unit.id = data.units?.get(key)?.get(innerKey)?.get("id").toString()
                            unit.name = data.units?.get(key)?.get(innerKey)?.get("name").toString()
                            unit.abbr = data.units?.get(key)?.get(innerKey)?.get("abbr").toString()
                            unit.factor = data.units?.get(key)?.get(innerKey)?.get("factor") as Double
                            unit.active = true
                            unit.deleted = false
                            unit.userId = CommonConstants.ANONYMOUS
                            unit.unitCategoryEntityId = key
                            result.add(unit)
                        }
                    }
                    event.onNext(result)
                } else {
                    event.onError(Exception(handler.cause()))
                }
            })
        })
    }

    /**
     * Generating all units from file: resources/currency.yaml
     */
    fun getCurrencies(vertx: Vertx) : Observable<List<Currency>> {
        return Observable.create({event ->
            vertx.fileSystem().readFile(model.currencyPath(), { handler ->
                if (handler.succeeded()) {
                    val result = mutableListOf<Currency>()
                    val yamlString = handler.result().toString()
                    val constructor = Constructor(CurrencyConfigSchema::class.java)
                    val yaml = Yaml(constructor)
                    val data = yaml.loadAs(yamlString, CurrencyConfigSchema::class.java)
                    data.currencies?.keys?.forEach { key ->
                        val currency = Currency("", "", false)
                        currency.id = key
                        currency.name = data.currencies?.get(key)?.get("name").toString()
                        currency.abbr = data.currencies?.get(key)?.get("abbr").toString()
                        currency.main = data.currencies?.get(key)?.get("isMain") as Boolean
                        currency.active = data.currencies?.get(key)?.get("active") as Boolean
                        currency.deleted = false
                        currency.userId = CommonConstants.ANONYMOUS
                        result.add(currency)
                    }
                    event.onNext(result)
                } else {
                    event.onError(Exception(handler.cause()))
                }
            })
        })
    }

}

object KeycloakConfig {

    lateinit var model: KeycloakConfigModel

    fun initKeycloakConfig(vertx: Vertx) : Observable<Boolean>{
        return Observable.create({ event ->
            vertx.fileSystem().readFile("${System.getProperty("user.dir")}/src/main/resources/keycloak-config.yaml", { handler ->
                if (handler.succeeded()) {
                    val yamlString = handler.result().toString()
                    val constructor = Constructor(KeycloakConfigSchema::class.java)
                    val yaml = Yaml(constructor)
                    val data = yaml.loadAs(yamlString, KeycloakConfigSchema::class.java)
                    KeycloakConfig.model = KeycloakConfigModel(
                            (data.keycloak as? Map<*, *>)?.get("admin").toString(),
                            (data.keycloak as? Map<*, *>)?.get("password").toString(),
                            (data.keycloak as? Map<*, *>)?.get("public_key").toString(),
                            (data.keycloak as? Map<*, *>)?.get("bearer_client_id").toString(),
                            (data.keycloak as? Map<*, *>)?.get("bearer_client_secret").toString(),
                            (data.keycloak as? Map<*, *>)?.get("token_client_id").toString(),
                            (data.keycloak as? Map<*, *>)?.get("token_client_secret").toString(),
                            (data.keycloak as? Map<*, *>)?.get("realm").toString(),
                            (data.keycloak as? Map<*, *>)?.get("auth_endpoint").toString(),
                            (data.keycloak as? Map<*, *>)?.get("access_token_endpoint").toString(),
                            (data.keycloak as? Map<*, *>)?.get("user_info_endpoint").toString()
                    )
                    event.onNext(true)
                } else {
                    event.onError(Exception(handler.cause()))
                }
            })

        })
    }

}

object DbConfig {

    lateinit var model: DbConfigModel

    fun initDbConfig(vertx: Vertx) : Observable<Boolean>{
        return Observable.create({ event ->
            vertx.fileSystem().readFile("${System.getProperty("user.dir")}/src/main/resources/db-config.yaml", { handler ->
                if (handler.succeeded()) {
                    val yamlString = handler.result().toString()
                    val constructor = Constructor(DbConfigSchema::class.java)
                    val yaml = Yaml(constructor)
                    val data = yaml.loadAs(yamlString, DbConfigSchema::class.java)
                    DbConfig.model = DbConfigModel(
                            (data.db as? Map<*, *>)?.get("uri").toString(),
                            (data.db as? Map<*, *>)?.get("common_db_name").toString(),
                            (data.db as? Map<*, *>)?.get("sign_up_db_name").toString()
                    )
                    event.onNext(true)
                } else {
                    event.onError(Exception(handler.cause()))
                }
            })

        })
    }

}

data class KeycloakConfigModel(
        var admin: String,
        var password: String,
        var publicKey: String,
        var bearerClientId: String,
        var bearerClientSecret: String,
        var tokenClientId: String,
        var tokenClientSecret: String,
        var realm: String,
        var auth_endpoint: String,
        var accessTokenEndpoint: String,
        var userInfoEndpoint: String)

data class ResourcePath(val unit: String, val currency: String, val country: String)
data class SystemConfigModel(val host: String, val port: String, val resourceHost: String, val resourcePort: String, val resourcePath: ResourcePath) {
    fun baseURL() : String { return "http://$host:$port" }
    fun resourceURL() : String { return "$resourceHost:$resourcePort" }
    fun currencyPath() : String { return "${System.getProperty("user.dir")}${resourcePath.currency}" }
    fun countryPath() : String { return "${System.getProperty("user.dir")}${resourcePath.country}" }
    fun unitPath() : String { return "${System.getProperty("user.dir")}${resourcePath.unit}" }
}

data class DbConfigModel(val uri: String, val commonDbName: String, val signUpDbName: String)

