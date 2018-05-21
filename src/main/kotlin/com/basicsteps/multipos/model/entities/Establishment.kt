package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity

@Entity
data class Establishment(@SerializedName("name") var name: String,
                         @SerializedName("address") var address: String?,
                         @SerializedName("phone") var phone: String?,
                         @SerializedName("pos_ids") var posIds: List<String>,
                         @SerializedName("stock_ids") var stockIds: List<String>,
                         @SerializedName("image_url") var imageUrl: String?) : BaseModel() {



    constructor() : this("", "", "", listOf(), listOf(), "")

    override fun instance(): Instanceable {

        val result = Establishment()
        result.createdTime = createdTime
        result.modifiedTime = modifiedTime
        result.createdBy = createdBy
        result.modifiedBy = modifiedBy
        result.active = active
        result.deleted = deleted
        result.userId = userId
        result.rootId = rootId
        result.access = access

        //spec
        result.name = name
        result.address = address
        result.phone = phone
        result.posIds = posIds
        result.stockIds = stockIds
        result.imageUrl = imageUrl

        return result
    }
}

data class EstablishmentResponse(@SerializedName("establishment") var establishment: Establishment, @SerializedName("stock_ids") var stockIds: List<String>)