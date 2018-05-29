package com.basicsteps.multipos.worker.handling.dao

/**
 * Created by Ikrom Mirzayev on 28-May-18.
 */
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.Payment
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class PaymentDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Payment>(dbManager, dataStore, Payment::class.java)