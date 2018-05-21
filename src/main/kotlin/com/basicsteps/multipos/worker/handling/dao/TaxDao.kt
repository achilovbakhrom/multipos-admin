package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.Tax
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class TaxDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Tax>(dbManager, dataStore, Tax::class.java)