package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.model.exceptions.NotExistsException
import com.basicsteps.multipos.core.model.exceptions.ReadDbFailedException
import com.basicsteps.multipos.model.InventoryOperation
import com.basicsteps.multipos.model.entities.Inventory
import com.basicsteps.multipos.model.entities.ListItem
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore
import io.reactivex.Observable

class InventoryDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Inventory>(dbManager, dataStore, Inventory::class.java){
    fun addOperation(idList: List<ListItem>, orderId: String, operationType: Int) : Observable<List<Inventory>> {
        return Observable.create({ event ->
            val count = idList.count()
            var counter = 0
            var listItem: ListItem = ListItem()
            val result = mutableListOf<Inventory>()
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
                        var inventory: Inventory = Inventory()
                        inventory.sourceId = orderId
                        inventory.operation = operationType
                        inventory.vendorId = it.vendorId
                        inventory.productId = it.productId
                        inventory.quantity = listItem.quantity
                        inventory.unitId = it.unitId
                        inventory.warehouseId = listItem.warehouseId

                        save(inventory)
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

    fun findByProductId(id: String): Observable<Inventory> {
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