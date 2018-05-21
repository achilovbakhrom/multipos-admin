package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity

@Entity
data class PaymentType(@SerializedName("name") var name: String,
                       @SerializedName("currencyId") var currencyId: String,
                       @SerializedName("accountId") var accountId: String) : BaseModel() {

    constructor(): this("", "", "")

    override fun instance(): Instanceable {
        val result = PaymentType()
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
        result.currencyId = currencyId
        result.accountId = accountId
        return result
    }

}