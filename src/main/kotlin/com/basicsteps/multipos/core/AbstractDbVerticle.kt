package com.basicsteps.multipos.core

import io.vertx.core.AbstractVerticle

abstract class AbstractDbVerticle constructor(val dbManager: DbManager) : AbstractVerticle() {

    companion object {
        val TENANT_ID_CHANNEL = "TENANT_ID_CHANNEL"
    }

    override fun start() {
        super.start()
        vertx.eventBus().consumer<String>(TENANT_ID_CHANNEL, {handler ->
            setTenantId(handler.body().toString())
        })
    }

    private fun setTenantId(tenantId: String) {
        dbManager.setTenantId(tenantId)
    }
}