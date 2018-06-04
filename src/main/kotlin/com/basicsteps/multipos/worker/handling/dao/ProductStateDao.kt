package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.ProductState
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class ProductStateDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<ProductState>(dbManager, dataStore, ProductState::class.java)