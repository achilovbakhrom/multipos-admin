package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity
import java.io.Serializable
import java.util.*

/**
 * Created by Ikrom Mirzayev on 21-May-18.
 */

@Entity
data class ExchangeRate(@SerializedName("source_id") var sourceId: String,
                    @SerializedName("target_id") var targetId: String,
                    @SerializedName("rate") var rate: Double,
                    @SerializedName("date") var date: Long) : BaseModel(), Serializable {

    constructor() : this("", "", 0.0, 0L)

    override fun instance() : Instanceable {
        val result = ExchangeRate()

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

        result.sourceId = sourceId
        result.targetId = targetId
        result.rate = rate
        result.date = date

        return result
    }
}