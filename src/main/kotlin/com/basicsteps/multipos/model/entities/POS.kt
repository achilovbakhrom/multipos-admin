package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity

@Entity
data class POS(@SerializedName("name") var name: String,
               @SerializedName("login") var login: String?,
               @SerializedName("password") var password: String?,
               @SerializedName("establishment_id") var establishmentId: String?,
               @SerializedName("image_url") var imageUrl: String?) : BaseModel() {

    constructor() : this("", "", "", "", "")

    override fun instance(): Instanceable {
        val result = POS()

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
        result.login = login
        result.password = password
        result.imageUrl = imageUrl
        result.establishmentId = establishmentId

        return result
    }

}

@Entity
data class POSResponse(var pos: POS, @SerializedName("stock_ids")var stockIds: List<String>)