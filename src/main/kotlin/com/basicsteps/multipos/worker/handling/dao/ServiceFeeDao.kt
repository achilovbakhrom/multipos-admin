package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.ServiceFee
import com.basicsteps.multipos.model.entities.Stock
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class ServiceFeeDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<ServiceFee>(dbManager, dataStore, ServiceFee::class.java)