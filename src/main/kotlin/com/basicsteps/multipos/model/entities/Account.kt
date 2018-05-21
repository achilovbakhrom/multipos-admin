package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.basicsteps.multipos.model.AccountType
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity

@Entity
data class Account(@SerializedName("name") var name: String,
                   @SerializedName("type") var type: Int) : BaseModel() {

    override fun instance(): Instanceable {
        val result = Account()

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
        result.type = type
        return result
    }

    constructor() : this("", AccountType.CASH.value())

}