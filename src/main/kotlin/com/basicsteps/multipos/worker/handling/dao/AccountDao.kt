package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.Account
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class AccountDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Account>(dbManager, dataStore, Account::class.java)