package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.Warehouse
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class WarehouseDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Warehouse>(dbManager, dataStore, Warehouse::class.java)