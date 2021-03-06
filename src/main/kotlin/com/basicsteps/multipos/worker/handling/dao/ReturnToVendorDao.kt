package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.ReturnToVendor
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class ReturnToVendorDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<ReturnToVendor>(dbManager, dataStore, ReturnToVendor::class.java)