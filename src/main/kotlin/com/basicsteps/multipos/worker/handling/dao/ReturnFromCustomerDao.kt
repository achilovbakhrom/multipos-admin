package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.ReturnFromCustomer
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class ReturnFromCustomerDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<ReturnFromCustomer>(dbManager, dataStore, ReturnFromCustomer::class.java)