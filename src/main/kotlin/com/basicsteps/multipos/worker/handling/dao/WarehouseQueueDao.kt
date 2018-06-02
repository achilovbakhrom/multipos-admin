package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.WarehouseQueue
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class WarehouseQueueDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<WarehouseQueue>(dbManager, dataStore, WarehouseQueue::class.java)