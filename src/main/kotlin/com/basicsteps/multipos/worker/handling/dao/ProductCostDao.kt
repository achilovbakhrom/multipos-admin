package com.basicsteps.multipos.worker.handling.dao


import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.Product
import com.basicsteps.multipos.model.entities.ProductCost
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class ProductCostDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<ProductCost>(dbManager, dataStore, ProductCost::class.java)