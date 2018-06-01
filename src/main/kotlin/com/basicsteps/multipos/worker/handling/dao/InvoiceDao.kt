package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.Invoice
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class InvoiceDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Invoice>(dbManager, dataStore, Invoice::class.java)