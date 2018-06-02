package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import com.sun.org.apache.xpath.internal.operations.Bool
import de.braintags.io.vertx.pojomapper.annotation.Entity
import de.braintags.io.vertx.pojomapper.annotation.field.Embedded
import java.io.Serializable

/**
 * Created by Ikrom Mirzayev on 21-May-18.
 */
//TODO refine parameters

@Entity
data class IncomingProduct(@SerializedName("product_id") var productId: String?,
                           @SerializedName("quantity") var quantity: Double?,
                           @SerializedName("unit_id") var unitId: String?,
                           @SerializedName("cost") var cost: Double?,
                           @SerializedName("purchased") var purchased: Boolean?) : BaseModel(), Serializable {

    constructor() : this("",0.0, "", 0.0, false)

    override fun instance() : Instanceable {
        val result = IncomingProduct()

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
        result.cost = cost
        result.purchased = purchased

        return result
    }
}