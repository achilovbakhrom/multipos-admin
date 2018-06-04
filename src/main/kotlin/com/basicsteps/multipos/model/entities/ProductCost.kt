package com.basicsteps.multipos.model.entities


import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity
import de.braintags.io.vertx.pojomapper.annotation.field.Embedded

@Entity
data class ProductCost(@SerializedName("date") var date: Long?,
                       @SerializedName("product_id") var productId: String?,
                       @SerializedName("cost") var cost: Double?,
                       @SerializedName("currency_id") var currencyId: String?,
                       @SerializedName("vendor_id") var vendorId: String?) : BaseModel() {

    //TODO attach list of vendors posible?
    constructor(): this(0, "", 0.0, "", "")

    override fun instance(): Instanceable {
        val result = ProductCost()
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
        result.date = date
        result.productId = productId
        result.cost = cost
        result.currencyId = currencyId
        result.vendorId = vendorId

        return result
    }
}