package com.basicsteps.multipos.model.entities


import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity
import de.braintags.io.vertx.pojomapper.annotation.field.Embedded
import java.io.Serializable
import java.util.*

/**
 * Created by Ikrom Mirzayev on 21-May-18.
 */

@Entity
data class Order(@Embedded @SerializedName("list_of_products") var listOfProducts: List<ListItem>?,
                 @Embedded @SerializedName("list_of_payments") var listOfPayments: List<Payment>?,
                 @SerializedName("assigned_discount") var assignedDiscount: String?,
                 @SerializedName("assigned_service_fee") var assignedServiceFee: String?,
                 @SerializedName("sales_person") var salesPerson: String?,
                 @SerializedName("customer") var customer: String?,
                 @SerializedName("status") var status: Int?,
                 @Embedded @SerializedName("list_of_coin") var listOfCoin: List<Coin>?,
                 @SerializedName("total_to_pay") var totalToPay: Double?,
                 @Embedded @SerializedName("order_history") var orderHistory: List<OrderHistory>?) : BaseModel(), Serializable {

    //TODO Order History

    constructor() : this(listOf(), listOf(), "", "", "", "", 0, listOf(), 0.0, listOf())

    override fun instance() : Instanceable {
        val result = Order()

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
        result.assignedServiceFee = assignedServiceFee
        result.salesPerson = salesPerson
        result.customer = customer
        result.status = status
        result.listOfCoin = listOfCoin
        result.totalToPay = totalToPay
        result.orderHistory = orderHistory

        return result
    }
}

@Entity
data class OrderHistory(@SerializedName("status") var status: String?,
                        @SerializedName("employee") var employee: String?,
                        @SerializedName("action") var action: String?,
                        @SerializedName("date_time") var dateTime: String?,
                        @SerializedName("reason") var reason: String?,
                        @SerializedName("description") var description: String?) : BaseModel(), Serializable {

    //TODO Order History

    constructor() : this("", "", "", "", "", "")

    override fun instance() : Instanceable {
        val result = OrderHistory()

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

        result.status = status
        result.employee = employee
        result.action = action
        result.dateTime = dateTime
        result.reason = reason
        result.description = description
        result.status = status

        return result
    }
}

@Entity
data class ListItem(@SerializedName("product_id") var productId: String?,
                    @SerializedName("quantity") var quantity: Double?,
                    @SerializedName("unit_id") var unitId: String?,
                    @SerializedName("cost") var cost: Double?) : BaseModel(), Serializable {

    //TODO Order History

    constructor() : this("", 0.0, "", 0.0)

    override fun instance() : Instanceable {
        val result = ListItem()

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

        return result
    }
}