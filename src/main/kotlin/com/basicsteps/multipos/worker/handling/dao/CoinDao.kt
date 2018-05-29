package com.basicsteps.multipos.worker.handling.dao

/**
 * Created by Ikrom Mirzayev on 26-May-18.
 */
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.Coin
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class CoinDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Coin>(dbManager, dataStore, Coin::class.java)