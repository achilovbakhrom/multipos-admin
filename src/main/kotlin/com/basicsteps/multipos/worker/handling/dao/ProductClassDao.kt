package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.ProductClass
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class ProductClassDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<ProductClass>(dbManager, dataStore, ProductClass::class.java)