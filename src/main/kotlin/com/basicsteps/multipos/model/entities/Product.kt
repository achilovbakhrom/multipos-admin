package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity
import de.braintags.io.vertx.pojomapper.annotation.field.Embedded

@Entity
data class Product(@SerializedName("subcategory_id") var subcategoryId: String,
                   @SerializedName("barcode") var barCode: String,
                   @SerializedName("sku") var sku: String,
                   @SerializedName("name") var name: String,
                   @SerializedName("unit_category_id") var unitCategoryId: String,
                   @SerializedName("unit_id") var unitId: String,
                   @SerializedName("product_class_id") var productClassId: String,
                   @SerializedName("description") var description: String,
                   @SerializedName("ingredient") var ingredient: Boolean,
                   @SerializedName("has_ingredient") var hasIngredient: Boolean,
                   @SerializedName("country") var country: String,
                   @SerializedName("price") var price: Double,
                   @SerializedName("price_currency_id") var priceCurrencyId: String,
                   @Embedded @SerializedName("product_has_ingridient") var productHasIngredient: List<ProductHasIngridient>?,
                   @SerializedName("tax_ids") var taxIds: List<String>?,
                   @SerializedName("taxes") var taxes: List<Tax>?) : BaseModel() {

    //TODO attach list of vendors posible?
    constructor(): this(
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            false,
            false,
            "",
            0.0,
            "",
            listOf(),
            listOf(),
            listOf())

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
        result.barCode = barCode
        result.sku = sku
        result.name = name
        result.unitCategoryId = unitCategoryId
        result.unitId = unitId
        result.productClassId = productClassId
        result.description = description
        result.ingredient = ingredient
        result.hasIngredient = hasIngredient
        result.country = country
        result.price = price
        result.priceCurrencyId = priceCurrencyId
        result.productHasIngredient = productHasIngredient
        result.taxIds = taxIds
        result.taxes = taxes

        return result
    }
}

@Entity
data class ProductHasIngridient(@SerializedName("product_id") var productId: String?,
                                @SerializedName("quantity") var quantity: Double?
) : BaseModel() {

    constructor() :this (
            "",
            0.0
    )

    override fun instance(): Instanceable {
        val result = ProductHasIngridient()
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
        result.productId = productId
        result.quantity = quantity

        return result
    }
}