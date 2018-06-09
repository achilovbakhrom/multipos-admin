package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity
import de.braintags.io.vertx.pojomapper.annotation.field.Embedded
import java.io.Serializable


@Entity
data class ReturnToVendor(@Embedded @SerializedName("list_of_products") var listOfProducts: List<ListItem>?,
                          @Embedded @SerializedName("list_of_payments") var listOfPayments: List<Payment>?,
                          @SerializedName("employee_id") var employeeId: String?,
                          @SerializedName("vendor_id") var vendorId: String?,
                          @SerializedName("total_to_pay") var totalToPay: Double?) : BaseModel(), Serializable {

    //TODO Order History

    constructor() : this(listOf(), listOf(), "", "", 0.0)

    override fun instance() : Instanceable {
        val result = ReturnToVendor()

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

        result.listOfProducts = listOfProducts
        result.listOfPayments = listOfPayments
        result.totalToPay = totalToPay
        result.employeeId = employeeId
        result.vendorId = vendorId

        return result
    }
}