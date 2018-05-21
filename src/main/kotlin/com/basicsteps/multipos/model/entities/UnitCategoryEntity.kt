package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.basicsteps.multipos.worker.handling.dao.UnitCategoryEntityDao
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity
import de.braintags.io.vertx.pojomapper.annotation.field.Embedded

@Entity
open class UnitCategoryEntity(@SerializedName("name") var name: String,
                              @SerializedName("unit_list") var unitList: List<UnitEntity>?) : BaseModel() {

    constructor() : this("", null)

    override fun instance(): Instanceable {
        val result = UnitCategoryEntity()

        //base
        result.createdTime = createdTime
        result.modifiedTime = modifiedTime
        result.createdBy = createdBy
        result.modifiedBy = modifiedBy
        result.active = active
        result.deleted = deleted
        result.userId = userId
        result.rootId = rootId
        result.modifiedId = modifiedId
        result.posId = posId
        result.access = access

        //specific
        result.name = name
        result.unitList = unitList
        return result
    }
}


