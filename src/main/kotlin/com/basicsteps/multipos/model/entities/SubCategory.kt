package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity

@Entity
data class SubCategory(@SerializedName("name") var name: String,
                       @Transient @SerializedName("category_id") var categoryId: String) : BaseModel() {

    @SerializedName("category")
    lateinit var category: Category


    constructor() : this("", "")

    override fun instance(): Instanceable {
        val result = SubCategory()

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

        //specification
        result.name = name
        result.categoryId = categoryId
        result.category = category

        return result
    }

    override fun toJson(): String {

        return super.toJson()
    }


}