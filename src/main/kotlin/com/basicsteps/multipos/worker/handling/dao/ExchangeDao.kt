package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.Exchange
import com.basicsteps.multipos.model.entities.Account
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class ExchangeDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Exchange>(dbManager, dataStore, Exchange::class.java)