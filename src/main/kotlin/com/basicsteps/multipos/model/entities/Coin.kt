package com.basicsteps.multipos.model.entities

/**
 * Created by Ikrom Mirzayev on 26-May-18.
 */

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.basicsteps.multipos.model.AccountType
import com.basicsteps.multipos.model.DocumentType
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity

@Entity
data class Coin(@SerializedName("amount") var amount: Double,
                @SerializedName("payment_type_ids") var paymentTypeIds: List<Int>,
                @SerializedName("payment_types") var paymentTypes: List<PaymentType>,
                @SerializedName("document_id") var documentId: Int,
                @SerializedName("document_type") var documentType: Int,
                @SerializedName("description") var description: String) : BaseModel() {

    override fun instance(): Instanceable {
        val result = Coin()

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
        result.paymentTypeIds = paymentTypeIds
        result.paymentTypes = paymentTypes
        result.documentId = documentId
        result.documentType = documentType
        result.description = description
        return result
    }

    constructor() : this(0.0, listOf(), listOf(), AccountType.CASH.value(), DocumentType.SALE.value(), "")

}