package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity
import de.braintags.io.vertx.pojomapper.annotation.field.Embedded

@Entity
data class CustomerGroup(@SerializedName("name") var name: String?,
                         @SerializedName("customers_id_list") var customersIdList: List<String>?) : BaseModel() {

    constructor() : this(null, listOf())

    override fun instance(): Instanceable {
        val result = CustomerGroup()
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
        result.customersIdList = customersIdList

        return result
    }
}