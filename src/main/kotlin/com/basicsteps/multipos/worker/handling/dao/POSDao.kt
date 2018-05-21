package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.POS
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class POSDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<POS>(dbManager, dataStore, POS::class.java)