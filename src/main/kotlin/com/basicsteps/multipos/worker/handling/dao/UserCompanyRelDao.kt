package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.core.model.exceptions.ReadDbFailedException
import com.basicsteps.multipos.model.UserCompanyRel
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore
import io.reactivex.Observable


class UserCompanyRelDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<UserCompanyRel>(dbManager, dataStore, UserCompanyRel::class.java) {


    fun trashByTenantId(tenantId: String, userId: String, companyId: String? = null): Observable<List<UserCompanyRel>> {
        return Observable.create({event ->
            var counter = 0
            var count = 0
            val result = mutableListOf<UserCompanyRel>()
            findAll().flatMapIterable({ list ->
                count = list.count()
                if (list.isEmpty()) {
                    event.onNext(listOf())
                }
                list
            }).flatMap({ item ->
                trash(item)
            }).subscribe({ deleted ->
                result.add(deleted)
                if (counter == count - 1) {
                    event.onNext(result)
                }
                counter++
            })
        })
    }


    fun getCompanyIdsByMail(email: String): Observable<List<String>> {
        return Observable.create({ event ->
            val query = dataStore?.createQuery(UserCompanyRel::class.java)
            query?.field("userName")?.`is`(email)
            query?.field("deleted")?.`is`(false)
            query?.execute({ result ->
                if (result.succeeded()) {
                    val iterator = result.result().iterator()
                    val res = mutableListOf<String>()
                    if (!result.result().isEmpty) {
                        val count = result.result().size()
                        var i = 0
                        while (iterator.hasNext()) {
                            iterator.next({item ->
                                if (item.succeeded()) {
                                    res.add(item.result().tenantId)
                                } else {
                                    event.onError(result.cause())
                                }
                            })
                            if (i == count - 1) {
                                event.onNext(res)
                            }
                            i++
                        }
                    } else {
                        event.onNext(res)
                    }
                } else {
                    event.onError(ReadDbFailedException())
                }
            })
        })
    }

}