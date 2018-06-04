package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.Inventory
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class InventoryDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Inventory>(dbManager, dataStore, Inventory::class.java)