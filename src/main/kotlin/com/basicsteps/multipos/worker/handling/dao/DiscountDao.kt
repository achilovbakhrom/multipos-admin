package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.Discount
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class DiscountDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Discount>(dbManager, dataStore, Discount::class.java)