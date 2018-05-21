package com.basicsteps.multipos.core.dao

import io.reactivex.Observable

interface Trash<T> {
    fun trash(t: T) : Observable<T>
    fun trash(elementId: String, userId: String) : Observable<T>
    fun trashAll(t: List<T>, userId: String) : Observable<List<T>>
    fun untrash(t: T) : Observable<T>
    fun untrashAll(t: List<T>, userId: String) : Observable<List<T>>
}
interface CRUDDao<T> : Trash<T>, Active<T> {
    fun save(t: T) : Observable<T>
    fun saveAll(t: List<T>, userId: String) : Observable<List<T>>
    fun update(t: T) : Observable<T>
    fun updateAll(t: List<T>, userId: String) : Observable<List<T>>
    fun updateWithoutDuplicate(t: T) : Observable<T>
    fun updateWithoutDuplicateAll(t: List<T>, userId: String) : Observable<List<T>>
    fun delete(t: T) : Observable<Boolean>
    fun deleteAll(t: List<T>) : Observable<Boolean>
    fun findById(id: String) : Observable<T>
    fun findAll(page: Int = 0, pageSize: Int = 0) : Observable<MutableList<T>>
}

interface Active<T> {
    fun activate(t: T) : Observable<T>
    fun inactivate(t: T) : Observable<T>
}
