package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.Establishment
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class EstablishmentDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Establishment>(dbManager, dataStore, Establishment::class.java)