package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.core.model.exceptions.DataNotFoundException
import com.basicsteps.multipos.core.model.exceptions.ReadDbFailedException
import com.basicsteps.multipos.model.Company
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore
import io.reactivex.Observable


class CompanyDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Company>(dbManager, dataStore, Company::class.java) {

    fun isIdentifierUnique(identifier: String) : Observable<Boolean> {
        return Observable.create({ event ->
            dbManager
                    ?.tenantsDao
                    ?.findAll()
                    ?.subscribe({ tenants ->
                        event.onNext(tenants.any { it.companyIdentifier == identifier })
                    })
        })
    }

    fun getCompanyByMail(email: String) : Observable<List<Company>> {
        return Observable.create({ event ->
            val result = mutableListOf<Company>()
            var count = 0
            var iterator = 0
            dbManager
                    ?.userCompanyRelDao
                    ?.getCompanyIdsByMail(email)
                    ?.map { items ->
                        count = items.size
                        if (items.isEmpty()) {
                            event.onNext(listOf())
                        }
                        items
                    }
                    ?.flatMapIterable { items -> items }
                    ?.flatMap({tenantId ->
                        getCompanyByTenantId(tenantId)
                    })
                    ?.subscribe({ company ->
                        result.add(company)
                        if (iterator == count-1) {
                            event.onNext(result)
                        }
                        iterator++
                    })
        })
    }

    fun isCompanyIdUnique(companyId: String): Observable<Boolean> {
        return Observable.create({event ->

        })
    }

    private fun getCompanyByTenantId(companyId: String) : Observable<Company> {
        return Observable.create({ event ->
            val query = dataStore?.createQuery(Company::class.java)
            query?.field("tenantId")?.`is`(companyId)
            query?.execute({ result ->
                if (result.succeeded()) {
                    val iterator = result.result().iterator()
                    if (iterator.hasNext()) {
                        iterator.next({ item ->
                            if (item.succeeded()) {
                                event.onNext(item.result()!!)
                            } else {
                                event.onError(item.cause())
                            }
                        })
                    } else {
                        event.onError(DataNotFoundException("data not found"))
                    }
                } else {
                    event.onError(ReadDbFailedException())
                }
            })
        })
    }

}