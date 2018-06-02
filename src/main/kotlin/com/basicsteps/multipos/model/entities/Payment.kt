package com.basicsteps.multipos.model.entities

/**
 * Created by Ikrom Mirzayev on 25-May-18.
 */

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.basicsteps.multipos.model.AccountType
import com.basicsteps.multipos.model.DocumentType
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity
import de.braintags.io.vertx.pojomapper.annotation.field.Embedded

@Entity
data class Payment(@SerializedName("document_type") var documentType: Int?,
                   @SerializedName("description") var description: String?,
                   @Embedded @SerializedName("payment_item_list") var paymentItemList: List<PaymentItem>?) : BaseModel() {

    override fun instance(): Instanceable {
        val result = Payment()

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
        result.documentType = documentType
        result.description = description
        result.paymentItemList = paymentItemList
        return result
    }

    constructor() : this(DocumentType.SALE.value(), "", listOf())

}
@Entity
data class PaymentItem (@SerializedName("amount") var amount: Double?,
                        @Embedded @SerializedName("payment_type") var paymentType: PaymentType?) : BaseModel() {
    override fun instance(): Instanceable {
        val result = PaymentItem()

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
        result.amount = amount
        result.paymentType = paymentType
        return result
    }

    constructor() : this(0.0, null)
}