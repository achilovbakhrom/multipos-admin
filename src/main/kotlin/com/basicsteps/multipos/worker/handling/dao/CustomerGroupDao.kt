package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.CustomerGroup
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class CustomerGroupDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<CustomerGroup>(dbManager, dataStore, CustomerGroup::class.java)