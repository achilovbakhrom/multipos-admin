package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.model.exceptions.DeleteDbFailedException
import com.basicsteps.multipos.core.model.exceptions.ReadDbFailedException
import com.basicsteps.multipos.core.model.exceptions.UpdateDbFailedException
import com.basicsteps.multipos.core.model.exceptions.WriteDbFailedException
import com.basicsteps.multipos.model.entities.CustomerVsCustomerGroup
import com.basicsteps.multipos.model.entities.WarehouseVsEstablishment
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore
import io.reactivex.Observable

class CustomerGroupVsCustomerDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<CustomerVsCustomerGroup>(dbManager, dataStore, CustomerVsCustomerGroup::class.java) {

    fun findCustomerIdsByCustomerGroupId(customerGroupId: String) : Observable<List<CustomerVsCustomerGroup>> {
        return Observable.create({
            if (dataStore != null) {
                val findQuery = dataStore?.createQuery(clazz)
                findQuery?.field("customerGroupId")?.`is`(customerGroupId)
                findQuery?.field("deleted")?.`is`(false)
                findQuery?.execute({ result ->
                    if (result.succeeded()) {
                        val iterator = result.result().iterator()
                        val res = mutableListOf<CustomerVsCustomerGroup>()
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

    fun updateCustomerListForCustomerGroup(customerGroupId: String, ids: List<String>, userId: String) : Observable<List<String>> {
        return Observable.create({event ->
            if (dataStore != null) {
                val findQuery = dataStore?.createQuery(clazz)
                findQuery?.field("customerGroupId")?.`is`(customerGroupId)
                findQuery?.field("deleted")?.`is`(false)
                findQuery?.execute({ result ->
                    if (result.succeeded()) {
                        val iterator = result.result().iterator()

                        val dbIds = mutableListOf<String>()
                        val cameIds = ids.toMutableList()

                        while (iterator.hasNext()) {
                            iterator.next { item ->
                                dbIds.add(item.result().id!!)
                            }
                        }


                        var isDelete = false
                        var count = dbIds.count()
                        var counter = 0
                        Observable
                                .fromArray(dbIds)
                                .flatMapIterable ({
                                    if (it.isEmpty()) {
                                        event.onNext(ids)
                                    }
                                    it

                                })
                                .map ({
                                    if (cameIds.contains(it)) {
                                        isDelete = false
                                        cameIds.remove(it)
                                    } else {
                                        isDelete = true
                                    }
                                    it
                                })
                                .flatMap({
                                    if (isDelete) {
                                        trash(it, userId)
                                    } else {
                                        Observable.just("")
                                    }
                                })
                                .flatMap({
                                    counter++
                                    if (counter == count && !cameIds.isEmpty()) {
                                        val savingList = mutableListOf<CustomerVsCustomerGroup>()
                                        for (tempId in cameIds) {
                                            val item = CustomerVsCustomerGroup()
                                            item.customerGroupId = customerGroupId
                                            item.customerId = tempId
                                            savingList.add(item)
                                        }
                                        saveAll(savingList, userId)
                                    } else {
                                        Observable.just("")
                                    }
                                })
                                .subscribe({
                                    if (counter == count) {
                                        event.onNext(ids)
                                    }
                                }, {
                                    event.onError(it)
                                })

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

    fun updateCustomerGroupListForCustomer(customerId: String, ids: List<String>, userId: String) : Observable<List<String>> {
        return Observable.create({event ->
            if (dataStore != null) {
                val findQuery = dataStore?.createQuery(clazz)
                findQuery?.field("customerId")?.`is`(customerId)
                findQuery?.field("deleted")?.`is`(false)
                findQuery?.execute({ result ->
                    if (result.succeeded()) {
                        val iterator = result.result().iterator()

                        val dbIds = mutableListOf<String>()
                        val cameIds = ids.toMutableList()

                        while (iterator.hasNext()) {
                            iterator.next { item ->
                                dbIds.add(item.result().id!!)
                            }
                        }


                        var isDelete = false
                        var count = dbIds.count()
                        var counter = 0
                        Observable
                                .fromArray(dbIds)
                                .flatMapIterable ({
                                    if (it.isEmpty()) {
                                        event.onNext(ids)
                                    }
                                    it

                                })
                                .map ({
                                    if (cameIds.contains(it)) {
                                        isDelete = false
                                        cameIds.remove(it)
                                    } else {
                                        isDelete = true
                                    }
                                    it
                                })
                                .flatMap({
                                    if (isDelete) {
                                        trash(it, userId)
                                    } else {
                                        Observable.just("")
                                    }
                                })
                                .flatMap({
                                    counter++
                                    if (counter == count && !cameIds.isEmpty()) {
                                        val savingList = mutableListOf<CustomerVsCustomerGroup>()
                                        for (tempId in cameIds) {
                                            val item = CustomerVsCustomerGroup()
                                            item.customerGroupId = tempId
                                            item.customerId = customerId
                                            savingList.add(item)
                                        }
                                        saveAll(savingList, userId)
                                    } else {
                                        Observable.just("")
                                    }
                                })
                                .subscribe({
                                    if (counter == count) {
                                        event.onNext(ids)
                                    }
                                }, {
                                    event.onError(it)
                                })

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