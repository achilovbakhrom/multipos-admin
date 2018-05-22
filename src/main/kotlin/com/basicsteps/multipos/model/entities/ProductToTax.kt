package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity
import java.io.Serializable

/**
 * Created by Ikrom Mirzayev on 22-May-18.
 */
@Entity
data class ProductToTax(@SerializedName("product_id") var productId: String,
                        @SerializedName("tax_id") var taxId: String) : BaseModel(), Serializable {

    constructor() : this("", "")

    override fun instance(): Instanceable {
        val result = ProductToTax()

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
        result.taxId = taxId

        return result
    }

}