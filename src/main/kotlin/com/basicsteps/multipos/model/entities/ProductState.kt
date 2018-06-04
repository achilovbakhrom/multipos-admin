package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity
import java.io.Serializable


@Entity
data class ProductState(@SerializedName("product_id") var productId: String?,
                        @SerializedName("quantity") var quantity: Double?,
                        @SerializedName("unit_id") var unitId: String?,
                        @SerializedName("vendor_id") var vendorId: String?,
                        @SerializedName("warehouse_id") var warehouseId: String?,
                        @SerializedName("purchased") var purchased: Boolean?) : BaseModel(), Serializable {

    constructor() : this("",0.0, "", "", "", false)

    override fun instance() : Instanceable {
        val result = ProductState()

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

        result.productId = productId
        result.quantity = quantity
        result.unitId = unitId
        result.vendorId = vendorId
        result.warehouseId = warehouseId
        result.purchased = purchased

        return result
    }
}