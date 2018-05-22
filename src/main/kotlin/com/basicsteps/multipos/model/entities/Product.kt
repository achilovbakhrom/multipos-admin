package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity

@Entity
data class Product(@SerializedName("subcategory_id") var subcategoryId: String,
                   @SerializedName("name") var name: String,
                   @SerializedName("unit_category_id") var unitCategoryId: String,
                   @SerializedName("unit_id") var unitId: String,
                   @SerializedName("product_class_id") var productClassId: String,
                   @SerializedName("description") var description: String,
                   @SerializedName("is_ingredient") var isIngredient: Boolean,
                   @SerializedName("has_ingredient") var hasIngredient: Boolean,
                   @SerializedName("ingredients") var ingredients: List<String>,
                   @SerializedName("country") var country: String,
                   @SerializedName("vendor_id") var vendorId: String,
                   @SerializedName("price") var price: Double,
                   @SerializedName("price_currency_id") var priceCurrencyId: String) : BaseModel() {

    //TODO attach list of vendors posible?
    constructor(): this(
            "",
            "",
            "",
            "",
            "",
            "",
            false,
            false,
            listOf(),
            "",
            "",
            0.0,
            "")

    override fun instance(): Instanceable {
        val result = Product()
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
        result.subcategoryId = subcategoryId
        result.name = name
        result.unitCategoryId = unitCategoryId
        result.unitId = unitId
        result.productClassId = productClassId
        result.description = description
        result.isIngredient = isIngredient
        result.hasIngredient = hasIngredient
        result.ingredients = ingredients
        result.country = country
        result.vendorId = vendorId
        result.price = price
        result.priceCurrencyId = priceCurrencyId

        return result
    }
}