package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.SurplusProduct
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class SurplusProductDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<SurplusProduct>(dbManager, dataStore, SurplusProduct::class.java)