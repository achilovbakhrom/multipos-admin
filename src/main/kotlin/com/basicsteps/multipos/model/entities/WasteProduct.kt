package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity
import de.braintags.io.vertx.pojomapper.annotation.field.Embedded
import java.io.Serializable


@Entity
data class WasteProduct(@Embedded @SerializedName("list_of_products") var listOfProducts: List<ListItem>?,
                        @SerializedName("employee_id") var employeeId: String?) : BaseModel(), Serializable {

    //TODO Order History

    constructor() : this(listOf(), "")

    override fun instance() : Instanceable {
        val result = WasteProduct()

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
        result.employeeId = employeeId

        return result
    }
}