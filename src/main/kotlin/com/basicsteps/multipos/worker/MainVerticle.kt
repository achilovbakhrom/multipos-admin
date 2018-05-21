package com.basicsteps.multipos.worker

import com.basicsteps.multipos.routing.OpenApiRoutingVerticle
import com.basicsteps.multipos.utils.DbConfig
import com.basicsteps.multipos.utils.KeycloakConfig
import com.basicsteps.multipos.utils.SystemConfig
import com.basicsteps.multipos.worker.handling.WorkerVerticle
import io.reactivex.Observable
import io.vertx.core.AbstractVerticle
import io.vertx.core.logging.LoggerFactory


class MainVerticle : AbstractVerticle() {

    val logger = LoggerFactory.getLogger(MainVerticle::class.java.name)

    override fun start() {
        super.start()
        //configuration

        Observable
                .just("")
                .flatMap({ _ ->
                    logger.info("initializing system config...")
                    SystemConfig.initSystemConfig(vertx)
                })
                .flatMap({ _ ->
                    logger.info("system config installed")

                    logger.info("-----------------------")
                    logger.info("initializing keycloak config")
                    KeycloakConfig.initKeycloakConfig(vertx)
                }).flatMap({ _ ->
                    logger.info("keycloak config installed")
                    logger.info("-----------------------")
                    logger.info("initializing db config")
                    DbConfig.initDbConfig(vertx)
                }).subscribe({ _ ->
                    logger.info("configuration fully completed")
                    vertx.deployVerticle(WorkerVerticle())
                    vertx.deployVerticle(OpenApiRoutingVerticle())
                })

    }
}