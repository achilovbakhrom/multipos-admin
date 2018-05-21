package com.basicsteps.multipos.core.dao

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.model.exceptions.*
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore
import io.reactivex.Observable


abstract class BaseDao<T : BaseModel> (val dbManager: DbManager?,
                                                      var dataStore: MongoDataStore?,
                                                      val clazz: Class<T>) : CRUDDao<T> {

    override fun trash(t: T): Observable<T> {
        return baseTrash(t, true)
    }

    override fun trash(elementId: String, userId: String): Observable<T> {
        return findById(elementId).flatMap({company ->
            company.userId = userId
            baseTrash(company, true)
        })
    }

    override fun untrash(t: T): Observable<T> {
        return baseTrash(t, false)
    }

    fun isRoot(t: T) : Observable<Boolean> { return Observable.create({ event -> event.onNext(t.rootId == null) }) }

    fun isModified(t: T) : Observable<Boolean> { return Observable.create({event -> event.onNext(t.modifiedTime == t.createdTime) }) }

    private fun baseTrash(t: T, isTrash: Boolean) : Observable<T> {
        return Observable.create({ event ->
            if (dataStore != null) {
                val saveQuery = dataStore?.createWrite(clazz)
                t.deleted = isTrash
                saveQuery?.add(t)
                saveQuery?.save({ result ->
                    if (result.succeeded()) {
                        event.onNext(t)
                    } else {
                        result.cause().printStackTrace()
                        event.onError(WriteDbFailedException())
                    }
                })
            } else {
                event.onError(DataStoreException("${this::class.java.name}: DataStore is not set yet..."))
            }
        })
    }

    override fun save(t: T): Observable<T> {
        return Observable.create({ event ->
            if (dataStore != null) {
                val saveQuery = dataStore?.createWrite(clazz)
                saveQuery?.add(t)
                saveQuery?.save({ result ->
                    if (result.succeeded()) {
                        event.onNext(t)
                    } else {
                        result.cause().printStackTrace()
                        event.onError(WriteDbFailedException())
                    }
                })
            } else {
                event.onError(DataStoreException("${this::class.java.name}: DataStore is not set yet..."))
            }
        })
    }



    override fun update(t: T): Observable<T> {
        return Observable.create({ event ->
            if (dataStore != null) {
                val copy = t.instance() as T
                trash(t).subscribe({result ->
                    if (t.rootId == null)
                        copy.rootId = t.id
                    else
                        copy.modifiedId = t.id
                    save(copy).subscribe({ _ -> event.onNext(copy) }, { error ->
                        error.printStackTrace()
                        event.onError(UpdateDbFailedException())
                    })
                }, { error ->
                    error.printStackTrace()
                    event.onError(UpdateDbFailedException())
                })
            } else {
                event.onError(DataStoreException("${this::class.java.name}: DataStore is not set yet..."))
            }
        })
    }

    override fun delete(t: T): Observable<Boolean> {
        return Observable.create({ event ->
            if (dataStore != null) {
                val deleteQuery = dataStore?.createDelete(clazz)
                if(t.rootId != null) {
                    val tempQuery = dataStore?.createQuery(clazz)
                    tempQuery?.field("root_id")?.`is`(t.rootId)
                    deleteQuery?.setQuery(tempQuery)
                }
                deleteQuery?.add(t)
                deleteQuery?.delete({ result ->
                    if (result.succeeded()) {
                        event.onNext(true)
                    } else {
                        result.cause().printStackTrace()
                        event.onError(DeleteDbFailedException())
                    }
                })
            } else {
                event.onError(DataStoreException("${this::class.java.name}: DataStore is not set yet..."))
            }
        })
    }

    override fun findById(id: String): Observable<T> {
        return Observable.create({ event ->
            if (dataStore != null) {
                val findQuery = dataStore?.createQuery(clazz)
                findQuery?.field("id")?.`is`(id)
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


    override fun findAll(page: Int, pageSize: Int): Observable<MutableList<T>> {
        return Observable.create({ event ->
            if (dataStore != null) {
                val query = dataStore?.createQuery(clazz)
                query?.field("deleted")?.`is`(false)
                if (pageSize > 0 && page >= 0) {
                    query?.setStart(page * pageSize)
                    query?.setLimit(pageSize)
                }
                query?.execute({ result ->
                    if (result.succeeded()) {
                        val iterator = result.result().iterator()
                        val res = ArrayList<T>()
                        while (iterator.hasNext()) {
                            iterator.next { item ->
                                if (item.succeeded()) {
                                    res.add(item.result())
                                } else {
                                    item.cause().printStackTrace()
                                    event.onError(ReadDbFailedException())
                                }
                            }
                        }
                        event.onNext(res)
                    } else {
                        result.cause().printStackTrace()
                        event.onError(ReadDbFailedException())
                    }
                })
            } else {
                event.onError(DataStoreException("${this::class.java.name}: DataStore is not set yet..."))
            }
        })
    }

    override fun activate(t: T): Observable<T> {
        return Observable.create({ event ->
            if (dataStore != null) {
                val saveQuery = dataStore?.createWrite(clazz)
                t.active = true
                saveQuery?.add(t)
                saveQuery?.save({ result ->
                    if (result.succeeded()) {
                        event.onNext(t)
                    }
                    else {
                        result.cause().printStackTrace()
                        event.onError(WriteDbFailedException())
                    }
                })
            } else {
                event.onError(DataStoreException("${this::class.java.name}: DataStore is not set yet..."))
            }
        })
    }

    override fun inactivate(t: T): Observable<T> {
        return Observable.create({ event ->
            if (dataStore != null) {
                val saveQuery = dataStore?.createWrite(clazz)
                t.active = false
                saveQuery?.add(t)
                saveQuery?.save({ result ->
                    if (result.succeeded()) {
                        event.onNext(t)
                    } else {
                        result.cause().printStackTrace()
                        event.onError(WriteDbFailedException())
                    }
                })
            } else {
                event.onError(DataStoreException("${this::class.java.name}: DataStore is not set yet..."))
            }
        })
    }

    override fun saveAll(t: List<T>, userId: String): Observable<List<T>> {
        return Observable.create({ event ->

            val saveQuery = dataStore?.createWrite(clazz)
            for (item in t) {
                item.active = true
                item.userId = userId
            }
            saveQuery?.addAll(t)
            saveQuery?.save({ result ->
                if (result.succeeded()) {
                    event.onNext(t)
                }
                else {
                    result.cause().printStackTrace()
                    event.onError(WriteDbFailedException())
                }
            })
        })
    }

    override fun updateAll(t: List<T>, userId: String): Observable<List<T>> {
        return Observable.create({ event ->
            if (dataStore != null) {
                val copies = mutableListOf<T>()
                for (item in t) {
                    val copy = item.instance() as T
                    copy.userId = userId
                    copies.add(copy)
                }
                var count = 0
                Observable
                        .fromArray(copies)
                        .flatMapIterable({ items -> items })
                        .flatMap({item -> update(item)})
                        .subscribe({_ ->
                            if (count == t.size -1) {
                                event.onNext(copies)
                            } else
                                count++
                        }, {error -> event.onError(error) })

            } else {
                event.onError(DataStoreException("${this::class.java.name}: DataStore is not set yet..."))
            }
        })
    }

    override fun updateWithoutDuplicate(t: T): Observable<T> { return save(t) }

    override fun updateWithoutDuplicateAll(t: List<T>, userId: String): Observable<List<T>> { return saveAll(t, userId) }

    override fun deleteAll(t: List<T>): Observable<Boolean> {
        return Observable
                .fromArray(t)
                .flatMapIterable({items -> items})
                .flatMap({ item -> delete(item)})
    }

    override fun trashAll(t: List<T>, userId: String): Observable<List<T>> {
        return Observable.create({ event ->
            if (dataStore != null) {
                var count = 0
                Observable
                        .fromArray(t)
                        .flatMapIterable({ items -> items })
                        .flatMap({item ->
                            item.userId = userId
                            trash(item)
                        })
                        .subscribe({_ ->
                            if (count == t.size -1) {
                                event.onNext(t)
                            } else
                                count++
                        }, {error -> event.onError(error) })

            } else {
                event.onError(DataStoreException("${this::class.java.name}: DataStore is not set yet..."))
            }
        })
    }

    override fun untrashAll(t: List<T>, userId: String): Observable<List<T>> {
        return Observable.create({ event ->
            if (dataStore != null) {
                var count = 0
                Observable
                        .fromArray(t)
                        .flatMapIterable({ items -> items })
                        .flatMap({item ->
                            item.userId = userId
                            untrash(item)
                        })
                        .subscribe({_ ->
                            if (count == t.size -1) {
                                event.onNext(t)
                            } else
                                count++
                        }, {error -> event.onError(error) })

            } else {
                event.onError(DataStoreException("${this::class.java.name}: DataStore is not set yet..."))
            }
        })
    }

}