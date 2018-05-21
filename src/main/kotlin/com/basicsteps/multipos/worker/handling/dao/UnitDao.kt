package com.basicsteps.multipos.worker.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.core.model.exceptions.DataNotFoundException
import com.basicsteps.multipos.core.model.exceptions.ReadDbFailedException
import com.basicsteps.multipos.model.entities.UnitCategoryEntity
import com.basicsteps.multipos.model.entities.UnitEntity
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore
import io.reactivex.Observable


class UnitCategoryEntityDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<UnitCategoryEntity>(dbManager, dataStore, UnitCategoryEntity::class.java)

class UnitEntityDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<UnitEntity>(dbManager, dataStore, UnitEntity::class.java) {

    fun getUnitsByUnitCategoryId(unitCategoryId: String) : Observable<List<UnitEntity>> {
        return Observable.create({event ->
            val query = dataStore?.createQuery(UnitEntity::class.java)
            query?.field("unitCategoryEntityId")?.`is`(unitCategoryId)
            query?.execute({ handler ->
                if (handler.succeeded()) {
                    if (handler.result() != null && !handler.result().isEmpty) {
                        val iterator = handler.result().iterator()
                        val result = mutableListOf<UnitEntity>()
                        while (iterator.hasNext()) { iterator.next({item -> result.add(item.result())}) }
                        event.onNext(result)
                    } else {
                        event.onError(DataNotFoundException("By $unitCategoryId data not found"))
                    }
                } else {
                    event.onError(ReadDbFailedException())
                }
            })
        })
    }
}