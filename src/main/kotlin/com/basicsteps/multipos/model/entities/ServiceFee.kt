package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity

enum class ServiceFeeType(val type: Int) {
    AMOUNT(0),
    PERCENT(1);
    fun value() = type
}

@Entity
data class ServiceFee(@SerializedName("name") var name: String,
                      @SerializedName("amount") var amount: Double,
                      @SerializedName("amount_currency") var amountCurrency: String?,
                      @SerializedName("type") var type: Int) : BaseModel() {

    constructor() : this("", 0.0, null, ServiceFeeType.AMOUNT.value())

    override fun instance(): Instanceable {
        val result = ServiceFee()
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
        result.name = name
        result.amount = amount
        result.type = type
        result.amountCurrency = amountCurrency
        return result
    }

}