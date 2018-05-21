package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity

@Entity
data class UnitEntity(@SerializedName("name") var name: String,
                      @SerializedName("abbr") var abbr: String,
                      @SerializedName("factor") var factor: Double,
                      @SerializedName("unit_category_entity_id") var unitCategoryEntityId: String) : BaseModel() {

    override fun instance(): Instanceable {
        val result = UnitEntity(name, abbr, factor, unitCategoryEntityId)

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
        result.abbr = abbr
        result.factor = factor
        result.unitCategoryEntityId = unitCategoryEntityId
        return result
    }

    constructor() : this("", "", 0.0, "")
}