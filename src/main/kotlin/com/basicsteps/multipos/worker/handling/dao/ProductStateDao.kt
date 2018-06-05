package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.ListItem
import com.basicsteps.multipos.model.entities.ProductState
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore
import io.reactivex.Observable
import kotlin.properties.Delegates

class ProductStateDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<ProductState>(dbManager, dataStore, ProductState::class.java){
    fun decreaseProductStateCount(idList: List<ListItem>) : Observable<List<ProductState>> {
        return Observable.create({ event ->
            val count = idList.count()
            var counter = 0
            var listItem: ListItem = ListItem()
            val result = mutableListOf<ProductState>()
            Observable
                    .fromArray(idList)
                    .flatMapIterable({
                        it
                    })
                    .flatMap({
                        listItem = it
                        findById(it.productId!!)
                    })
                    .flatMap ({
                        var quantity: Double by Delegates.notNull<Double>()
                        quantity = it.quantity!!
                        quantity -= listItem.quantity!!
                        it.quantity = quantity
                        update(it)
                    })
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