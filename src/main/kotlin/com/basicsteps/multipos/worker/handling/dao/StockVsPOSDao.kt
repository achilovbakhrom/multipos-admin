package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.model.exceptions.ReadDbFailedException
import com.basicsteps.multipos.model.entities.StockVsPOS
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore
import io.reactivex.Observable

class StockVsPOSDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<StockVsPOS>(dbManager, dataStore, StockVsPOS::class.java) {

    fun findStockIdsByPOSId(posId: String) : Observable<List<StockVsPOS>> {
        return Observable.create({
            if (dataStore != null) {
                val findQuery = dataStore?.createQuery(clazz)
                findQuery?.field("posId")?.`is`(posId)
                findQuery?.field("deleted")?.`is`(false)
                findQuery?.execute({ result ->
                    if (result.succeeded()) {
                        val iterator = result.result().iterator()
                        val res = mutableListOf<StockVsPOS>()
                        while (iterator.hasNext()) {
                            iterator.next { item ->
                                if (item.succeeded()) {
                                    res.add(item.result())
                                } else {
                                    item.cause().printStackTrace()
                                    it.onError(ReadDbFailedException())
                                }
                            }
                        }
                        it.onNext(res)
                    }
                    else {
                        result.cause().printStackTrace()
                        it.onError(ReadDbFailedException())
                    }
                })
            } else {
                it.onError(DataStoreException("${this::class.java.name}: DataStore is not set yet..."))
            }
        })
    }

}