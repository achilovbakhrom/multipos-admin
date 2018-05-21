package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity

enum class DiscountType(val type: Int) {
    AMOUNT(0),
    PERCENT(1);
    fun value() = type
}

enum class DiscountQualificationType(val qualificationType: Int) {
    ITEM(0),
    ORDER(1);
    fun value() = qualificationType
}

@Entity
data class Discount(@SerializedName("name") var name: String,
                    @SerializedName("type") var type: Int, // percent, amount
                    @SerializedName("qualification_type") var qualificationType: Int, // Item, Order, All
                    @SerializedName("amount") var amount: Double = 0.0,
                    @Transient @SerializedName("amount_currency_id") var amountCurrencyId: String?,
                    @SerializedName("amount_currency") var amountCurrency: Currency?) : BaseModel() {

    constructor() : this("", 0, 0, 0.0, null,null)

    override fun instance(): Instanceable {
        val result = Discount()

        //base
        result.createdTime = createdTime
        result.modifiedTime = modifiedTime
        result.createdBy = createdBy
        result.modifiedBy = modifiedBy
        result.active = active
        result.deleted = deleted
        result.userId = userId
        result.rootId = rootId
        result.access = access

        //specs
        result.name = name
        result.type = type
        result.qualificationType = qualificationType
        result.amount = amount
        result.amountCurrencyId = amountCurrencyId
        result.amountCurrency = amountCurrency

        return result
    }




}