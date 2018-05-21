package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.PaymentType
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class PaymentTypeDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<PaymentType>(dbManager, dataStore, PaymentType::class.java)