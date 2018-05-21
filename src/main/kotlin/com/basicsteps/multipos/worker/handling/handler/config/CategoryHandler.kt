package com.basicsteps.multipos.worker.handling.handler.config

import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.model.entities.Category
import com.basicsteps.multipos.worker.handling.dao.CategoryDao
import io.vertx.core.Vertx

class CategoryHandler(vertx: Vertx) : BaseCRUDHandler(vertx)