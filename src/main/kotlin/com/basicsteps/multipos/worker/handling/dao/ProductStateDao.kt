package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.model.exceptions.NotExistsException
import com.basicsteps.multipos.core.model.exceptions.ReadDbFailedException
import com.basicsteps.multipos.model.InventoryOperation
import com.basicsteps.multipos.model.entities.Inventory
import com.basicsteps.multipos.model.entities.ListItem
import com.basicsteps.multipos.model.entities.ProductState
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore
import io.reactivex.Observable
import javax.ws.rs.InternalServerErrorException

class ProductStateDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<ProductState>(dbManager, dataStore, ProductState::class.java){
    fun decreaseProductStateCountList(idList: List<ListItem>) : Observable<List<ProductState>> {
        return Observable.create({ event ->
            val count = idList.count()
            var counter = 0
            var listItem: ListItem = ListItem()
            val result = mutableListOf<ProductState>()
            var searchingId = ""
            Observable
                    .fromArray(idList)
                    .flatMapIterable({
                        it
                    })
                    .flatMap({
                        listItem = it
                        searchingId = it.productId!!
                        findByProductId(it.productId!!)
                    })
                    .flatMap ({
                        if(it is ProductState){
                            var quantity: Double = it.quantity!!
                            quantity -= listItem.quantity!!
                            it.quantity = quantity
                            updateWithoutDuplicate(it)
                        } else {
                            Observable.just("")
                        }

                    })
                    .subscribe({
                        if(it is ProductState){
                            result.add(it)
                            counter ++
                            if (counter == count) {
                                event.onNext(result)
                            }
                        } else {
                            event.onError(NotExistsException("productId", searchingId))
                        }

                    }, {
                        event.onError(it)
                    })

        })
    }

    fun changeProductStateCount(inventory: Inventory) : Observable<ProductState> {
        return Observable.create({ event ->
            Observable
                    .just(inventory)
                    .flatMap({
                        findByProductId(it.productId!!)
                    })
                    .flatMap({
                        if(it is ProductState){
                            var quantity: Double = it.quantity!!
                            if(inventory.operation == InventoryOperation.WASTE.value()){
                                quantity -= inventory.quantity!!
                            } else if(inventory.operation == InventoryOperation.SURPLUS.value()){
                                quantity += inventory.quantity!!
                            }
                            it.quantity = quantity
                            updateWithoutDuplicate(it)
                        } else {
                            Observable.just("")
                        }

                    })
                    .subscribe({
                        if (it is ProductState) {
                            event.onNext(it)
                        } else {
                            event.onError(NotExistsException("id", inventory.productId!!))
                        }
                    }, {
                        event.onError(it)
                    })

        })
    }

    fun saveAllIfNotExists(productStateList: List<ProductState>) : Observable<List<ProductState>> {
        return Observable.create({ event ->
            val count = productStateList.count()
            var counter = 0
            var productState: ProductState = ProductState()
            val result = mutableListOf<ProductState>()
            Observable
                    .fromArray(productStateList)
                    .flatMapIterable({
                        it
                    })
                    .flatMap({
                        productState = it
                        findByProductId(it.productId!!)
                    })
                    .flatMap ({
                        if (it is Boolean) {
                            save(productState)
                        } else if (it is ProductState) {
                            var quantity: Double = it.quantity!!
                            quantity += productState.quantity!!
                            it.quantity = quantity
                            updateWithoutDuplicate(it)
                        } else {
                            Observable.just("")
                        }
                    })
                    .subscribe({
                        if (it is ProductState) {
                            result.add(it)
                            counter ++
                            if (counter == count) {
                                event.onNext(result)
                            }
                        } else {
                            event.onError(InternalServerErrorException("Something went wrong!"))
                        }

                    }, {
                        event.onError(it)
                    })

        })
    }

    fun findByProductId(id: String): Observable<Any> {
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
                            event.onNext(false)
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