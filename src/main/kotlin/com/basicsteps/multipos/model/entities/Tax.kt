package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity
import kotlin.math.round

enum class TaxRoundingType(val type: Int) {
    MATH(0),
    CEILING(1);
    fun value() = type
}

@Entity
data class Tax(@SerializedName("name") var name: String,
               @SerializedName("rounding_type") var roundingType: Int,
               @SerializedName("tax_rate") var taxRate: Double,
               @SerializedName("from") var from: Long?,
               @SerializedName("to") var to: Long?) : BaseModel() {

    constructor() : this("", TaxRoundingType.MATH.value(), 0.0, null, null)

    override fun instance(): Instanceable {
        val result = Tax()

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
        result.roundingType = roundingType
        result.taxRate = taxRate
        result.from = from
        result.to = to

        return result

    }


}