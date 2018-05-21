package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.Currency
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class CurrencyDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Currency>(dbManager, dataStore, Currency::class.java)