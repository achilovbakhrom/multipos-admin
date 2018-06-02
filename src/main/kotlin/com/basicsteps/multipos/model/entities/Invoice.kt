package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity
import de.braintags.io.vertx.pojomapper.annotation.field.Embedded
import java.io.Serializable

/**
 * Created by Ikrom Mirzayev on 21-May-18.
 */
//TODO refine parameters

@Entity
data class Invoice(@Embedded @SerializedName("list_of_products") var listOfProducts: List<IncomingProduct>?,
                   @Embedded @SerializedName("list_of_payments") var listOfPayments: List<Payment>?,
                   @SerializedName("assigned_discount") var assignedDiscount: String?,
                   @SerializedName("employee") var employee: String?,
                   @SerializedName("vendor") var vendor: String?,
                   @SerializedName("type") var type: Int?,
                   @SerializedName("warehouse_id") var warehouseId: String?,
                   @SerializedName("currency_id") var currencyId: String?,
                   @SerializedName("total_to_pay") var totalToPay: Double?) : BaseModel(), Serializable {

    //TODO Order History

    constructor() : this(listOf(), listOf(), "","", "", 0, "", "",0.0)

    override fun instance() : Instanceable {
        val result = Invoice()

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
        result.assignedDiscount = assignedDiscount
        result.employee = employee
        result.vendor = vendor
        result.type = type
        result.warehouseId = warehouseId
        result.currencyId = currencyId
        result.totalToPay = totalToPay

        return result
    }
}