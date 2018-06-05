package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.InventoryOperation
import com.basicsteps.multipos.model.entities.Inventory
import com.basicsteps.multipos.model.entities.ListItem
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore
import io.reactivex.Observable

class InventoryDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Inventory>(dbManager, dataStore, Inventory::class.java){
    fun addSaleOperation(idList: List<ListItem>, orderId: String) : Observable<List<Inventory>> {
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
                        findById(it.productId!!)
                    })
                    .flatMap ({
                        var inventory: Inventory = Inventory()
                        inventory.sourceId = orderId
                        inventory.operation = InventoryOperation.SALE.value()
                        inventory.vendorId = it.vendorId
                        inventory.productId = it.productId
                        inventory.quantity = listItem.quantity
                        inventory.unitId = it.unitId

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
}