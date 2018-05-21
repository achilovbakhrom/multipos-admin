package com.basicsteps.multipos.model

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity

@Entity
data class Tenants(@SerializedName("tenant") var tenant: String,
                   @Transient @SerializedName("company_id") var companyIdentifier: String) : BaseModel() {

    constructor() : this("", "")

    override fun instance(): Instanceable {
        val result = Tenants()

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

        //specs
        result.tenant = tenant
        result.companyIdentifier = companyIdentifier
        return result
    }
    
}