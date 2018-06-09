package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.model.exceptions.NotExistsException
import com.basicsteps.multipos.core.model.exceptions.ReadDbFailedException
import com.basicsteps.multipos.model.QueueType
import com.basicsteps.multipos.model.entities.ListItem
import com.basicsteps.multipos.model.entities.WarehouseQueue
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore
import io.reactivex.Observable

class WarehouseQueueDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<WarehouseQueue>(dbManager, dataStore, WarehouseQueue::class.java){

    fun findByProductId(id: String): Observable<WarehouseQueue> {
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

    fun findByProductAndWarehouseId(id: String, warehouseId: String, queueType: Int?, queueId: String?): Observable<WarehouseQueue> {
        return Observable.create({ event ->
            var ascending = true
            if(queueType == QueueType.LIFO.value())
                ascending = false
            if (dataStore != null) {
                //TODO query quantity available > 0
                val findQuery = dataStore?.createQuery(clazz)
                findQuery?.field("incomingProductId")?.`is`(id)
                findQuery?.field("warehouseId")?.`is`(warehouseId)
                findQuery?.field("deleted")?.`is`(false)
                if (queueId != null && queueId != "")
                    findQuery?.field("id")?.`is`(queueId)
                if(queueType == QueueType.FEFO.value())
                    findQuery?.addSort("expiryDate", ascending)
                findQuery?.addSort("createdTime", ascending)
                findQuery?.setLimit(1)
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

    fun decreaseWarehouseQueueList(idList: List<ListItem>) : Observable<List<WarehouseQueue>> {
        return Observable.create({ event ->
            val count = idList.count()
            var counter = 0
            var listItem: ListItem = ListItem()
            val result = mutableListOf<WarehouseQueue>()
            Observable
                    .fromArray(idList)
                    .flatMapIterable({
                        it
                    })
                    .flatMap({
                        listItem = it
                        findByProductAndWarehouseId(it.productId!!, it.warehouseId!!, it.queueType, it.queueId)
//                        findByProductId(it.productId!!)
                    })
                    .flatMap ({
                        var quantityAvailable: Double = it.quantityAvailable!!
                        var quantitySold: Double = it.quantitySold!!
                        quantityAvailable -= listItem.quantity!!
                        quantitySold += listItem.quantity!!
                        it.quantityAvailable = quantityAvailable
                        it.quantitySold = quantitySold
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

}