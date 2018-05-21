package com.basicsteps.multipos.worker.handling.handler.config

import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.model.exceptions.ReadDbFailedException
import com.basicsteps.multipos.core.response.MultiPosResponse
import com.basicsteps.multipos.model.StatusMessages
import com.basicsteps.multipos.model.entities.UnitCategoryEntity
import com.basicsteps.multipos.worker.handling.dao.UnitCategoryEntityDao
import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.Observable
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject

class UnitCategoryHandler(vertx: Vertx) : BaseCRUDHandler(vertx)