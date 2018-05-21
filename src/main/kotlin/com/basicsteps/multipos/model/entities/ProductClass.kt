package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity

@Entity
data class ProductClass(@SerializedName("name") var name: String,
                        @SerializedName("description") var description: String) : BaseModel() {

    constructor() : this("", "")

    override fun instance(): Instanceable {
        val result = ProductClass()
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

        //specifications
        result.name = name
        result.description = description

        return result
    }
}

