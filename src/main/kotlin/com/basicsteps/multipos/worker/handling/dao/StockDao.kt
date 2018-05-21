package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.Stock
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class StockDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Stock>(dbManager, dataStore, Stock::class.java)