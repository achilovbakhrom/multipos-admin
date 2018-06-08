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
data class WarehouseQueue(@SerializedName("incoming_product_id") var incomingProductId: String?,
                          @SerializedName("warehouse_id") var warehouseId: String?,
                          @SerializedName("quantity_available") var quantityAvailable: Double?,
                          @SerializedName("quantity_sold") var quantitySold: Double?,
                          @SerializedName("quantity_received") var quantityReceived: Double?,
                          @SerializedName("source_id") var sourceId: String?,
                          @SerializedName("operation") var operation: Int?,
                          @SerializedName("expiry_date") var expiryDate: Long?,
                          @SerializedName("production_date") var productionDate: Long?) : BaseModel(), Serializable {

    constructor() : this("", "", 0.0, 0.0, 0.0, "", 0, 0, 0)

    override fun instance() : Instanceable {
        val result = WarehouseQueue()

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

        result.incomingProductId = incomingProductId
        result.warehouseId = warehouseId
        result.quantityAvailable = quantityAvailable
        result.quantitySold = quantitySold
        result.quantityReceived = quantityReceived
        result.sourceId = sourceId
        result.operation = operation
        result.expiryDate = expiryDate
        result.productionDate = productionDate

        return result
    }
}