package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.model.exceptions.NotExistsException
import com.basicsteps.multipos.core.model.exceptions.ReadDbFailedException
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
                        findByProductId(it.productId!!)
                    })
                    .flatMap ({
                        var quantity: Double = it.quantity!!
                        quantity -= listItem.quantity!!
                        it.quantity = quantity
                        updateWithoutDuplicate(it)
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


    fun findByProductId(id: String): Observable<ProductState> {
        return Observable.create({ event ->
            if (dataStore != null) {
                val findQuery = dataStore?.createQuery(clazz)
                findQuery?.field("productId")?.`is`(id)
                findQuery?.field("deleted")?.`is`(false)
                findQuery?.execute({ result ->
                    if (result.succeeded()) {
                        val iterator = result.result().iterator()
                        if (iterator.hasNext())
                            iterator.next({handler ->
                                if (handler.succeeded()) {
                                    event.onNext(handler.result())
                                } else {
                                    event.onError(ReadDbFailedException())
                                }
                            })
                        else {
                            event.onError(NotExistsException("id", id))
                        }
                    }
                    else {
                        result.cause().printStackTrace()
                        event.onError(ReadDbFailedException())
                    }
                })
            } else {
                event.onError(DataStoreException("${this::class.java.name}: DataStore is not set yet..."))
            }
        })
    }
}