package com.basicsteps.multipos.worker.handling.handler.config

import com.basicsteps.multipos.core.handler.BaseCRUDHandler
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.model.entities.SubCategory
import com.basicsteps.multipos.worker.handling.dao.SubCategoryDao
import io.vertx.core.Vertx

class SubCategoryHandler(vertx: Vertx) : BaseCRUDHandler(vertx)