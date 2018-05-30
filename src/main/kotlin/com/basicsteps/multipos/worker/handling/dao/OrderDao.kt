package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.Order
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class OrderDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Order>(dbManager, dataStore, Order::class.java)