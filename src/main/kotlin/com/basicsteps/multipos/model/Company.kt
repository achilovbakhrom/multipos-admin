package com.basicsteps.multipos.model

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity

@Entity
data class Company(@SerializedName("name") var name: String,
                   @SerializedName("logo_url") var logoUrl: String,
                   @SerializedName("address") var address: String,
                   @SerializedName("phone_number") var phoneNumber: String,
                   @SerializedName("zip_code") var zipCode: Int,
                   @SerializedName("type") var type: Int = CompanyType.B2C.value(),
                   @SerializedName("tenant_id") var tenantId: String) : BaseModel() {

    constructor() : this("",  "", "", "", 0, CompanyType.B2C.value(), "")

    override fun instance(): Instanceable {
        val result = Company()

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

        //spec
        result.name = name
        result.logoUrl = logoUrl
        result.address = address
        result.phoneNumber = phoneNumber
        result.zipCode = zipCode
        result.tenantId = tenantId
        result.type = type
        return result
    }
}