package com.basicsteps.multipos.model

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName


data class Exchange(@SerializedName("source_id") var sourceId: String,
                    @SerializedName("exchanging_id") var exchangingId: String,
                    @SerializedName("rate") var rate: Double) : BaseModel() {

    constructor() : this("", "", 0.0)

    override fun instance(): Instanceable {
        val result = Exchange()

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
        result.sourceId = sourceId
        result.exchangingId = exchangingId
        result.rate = rate

        return result
    }


}