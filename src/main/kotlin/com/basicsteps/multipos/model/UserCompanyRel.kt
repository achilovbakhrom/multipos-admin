package com.basicsteps.multipos.model

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity

@Entity
data class UserCompanyRel(@SerializedName("user_name") var userName: String,
                          @SerializedName("tenant_id") var tenantId: String) : BaseModel() {

    override fun instance(): Instanceable {
        val result = UserCompanyRel()

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
        result.userName = userName
        result.tenantId = tenantId
        return result
    }

    constructor() :  this("", "")
}