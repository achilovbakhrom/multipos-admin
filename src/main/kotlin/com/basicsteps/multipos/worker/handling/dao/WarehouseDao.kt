package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.model.exceptions.ReadDbFailedException
import com.basicsteps.multipos.model.entities.Warehouse
import com.basicsteps.multipos.model.entities.WarehouseVsEstablishment
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore
import io.reactivex.Observable

class WarehouseDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Warehouse>(dbManager, dataStore, Warehouse::class.java)