/**
 * Created by Ikrom Mirzayev on 24-May-18.
 */
package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity



@Entity
data class
ProductVSTax(@SerializedName("tax_id") var taxId: String,
             @SerializedName("product_id") var productId: String) : BaseModel() {

    constructor() : this("", "")

    override fun instance(): Instanceable {
        val result = ProductVSTax()

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

        //specifications
        result.taxId = taxId
        result.productId = productId

        return result
    }
}