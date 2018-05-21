package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.Product
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class ProductDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Product>(dbManager, dataStore, Product::class.java)