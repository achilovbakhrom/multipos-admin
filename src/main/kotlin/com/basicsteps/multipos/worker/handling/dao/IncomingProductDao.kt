package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.IncomingProduct
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class IncomingProductDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<IncomingProduct>(dbManager, dataStore, IncomingProduct::class.java)