package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.Tax
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore
import io.reactivex.Observable

class TaxDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Tax>(dbManager, dataStore, Tax::class.java){
    fun getTaxListByIds(idList: List<String>) : Observable<List<Tax>> {
        return Observable.create({ event ->
            val count = idList.count()
            var counter = 0
            val result = mutableListOf<Tax>()
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