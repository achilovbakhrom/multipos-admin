package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.POS
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore
import io.reactivex.Observable

class POSDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<POS>(dbManager, dataStore, POS::class.java) {

    fun getPOSListByIds(idList: List<String>) : Observable<List<POS>> {
        return Observable.create({ event ->
            val count = idList.count()
            var counter = 0
            val result = mutableListOf<POS>()
            Observable
                    .fromArray(idList)
                    .flatMapIterable({ it })
                    .flatMap({ findById(it) })
                    .subscribe({
                        result.add(it)
                        counter ++
                        if (counter == count) {
                            event.onNext(result)
                        }
                    }, {
                        event.onError(it)
                    })

        })
    }

}