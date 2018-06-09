package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.WasteProduct
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class WasteProductDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<WasteProduct>(dbManager, dataStore, WasteProduct::class.java)