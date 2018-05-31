package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity

@Entity
data class
CustomerVsCustomerGroup(@SerializedName("customer_id") var customerId: String,
                         @SerializedName("customer_group_id") var customerGroupId: String) : BaseModel() {

    constructor() : this("", "")

    override fun instance(): Instanceable {
        val result = CustomerVsCustomerGroup()

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
        result.customerGroupId = customerGroupId
        result.customerId = customerId


        return result
    }
}