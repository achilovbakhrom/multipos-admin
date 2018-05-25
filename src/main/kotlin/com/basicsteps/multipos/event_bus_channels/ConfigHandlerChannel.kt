package com.basicsteps.multipos.event_bus_channels

enum class ConfigHandlerChannel(val channel: String) {

    //Unit
    UNIT_GET("UNIT_GET"),
    UNIT_LIST("UNIT_LIST"),
    UNIT_UPDATE("UNIT_UPDATE"),

    //POS && STOCK
    POS_CREATE("POS_CREATE"),
    POS_LIST("POS_LIST"),
    POS_UPDATE("POS_UPDATE"),
    POS_GET("POS_GET"),
    POS_DELETE("POS_DELETE"),

    WAREHOUSE_CREATE("WAREHOUSE_CREATE"),
    WAREHOUSE_UPDATE("WAREHOUSE_UPDATE"),
    WAREHOUSE_LIST("WAREHOUSE_LIST"),
    WAREHOUSE_GET("WAREHOUSE_GET"),
    WAREHOUSE_DELETE("WAREHOUSE_DELETE"),
    LINK_POS_STOCK("LINK_POS_STOCK"),

    ESTABLISHMENT_CREATE("ESTABLISHMENT_CREATE"),
    ESTABLISHMENT_UPDATE("ESTABLISHMENT_UPDATE"),
    ESTABLISHMENT_LIST("ESTABLISHMENT_LIST"),
    ESTABLISHMENT_GET("ESTABLISHMENT_GET"),
    ESTABLISHMENT_DELETE("ESTABLISHMENT_DELETE"),

    //Account
    ACCOUNT_CREATE("ACCOUNT_CREATE"),
    ACCOUNT_LIST("ACCOUNT_LIST"),
    ACCOUNT_GET("ACCOUNT_GET"),
    ACCOUNT_UPDATE("ACCOUNT_UPDATE"),
    ACCOUNT_DELETE("ACCOUNT_DELETE"),

    //Currency
    CURRENCY_LIST("CURRENCY_LIST"),
    CURRENCY_GET("CURRENCY_GET"),
    CURRENCY_UPDATE("CURRENCY_UPDATE"),

    //PaymentType
    PAYMENT_TYPE_CREATE("PAYMENT_TYPE_CREATE"),
    PAYMENT_TYPE_LIST("PAYMENT_TYPE_LIST"),
    PAYMENT_TYPE_UPDATE("PAYMENT_TYPE_UPDATE"),
    PAYMENT_TYPE_GET("PAYMENT_TYPE_GET"),
    PAYMENT_TYPE_DELETE("PAYMENT_TYPE_DELETE"),

    //Vendor
    VENDOR_CREATE("VENDOR_CREATE"),
    VENDOR_LIST("VENDOR_LIST"),
    VENDOR_UPDATE("VENDOR_UPDATE"),
    VENDOR_DELETE("VENDOR_DELETE"),
    VENDOR_GET("VENDOR_GET"),

    //Product
    PRODUCT_CREATE("PRODUCT_CREATE"),
    PRODUCT_UPDATE("PRODUCT_UPDATE"),
    PRODUCT_LIST("PRODUCT_LIST"),
    PRODUCT_GET("PRODUCT_GET"),
    PRODUCT_TRASH("PRODUCT_TRASH"),

    //Product class
    PRODUCT_CLASS_CREATE("PRODUCT_CLASS_CREATE"),
    PRODUCT_CLASS_UPDATE("PRODUCT_CLASS_UPDATE"),
    PRODUCT_CLASS_LIST("PRODUCT_CLASS_LIST"),
    PRODUCT_CLASS_GET("PRODUCT_CLASS_GET"),
    PRODUCT_CLASS_DELETE("PRODUCT_CLASS_DELETE"),

    //Category
    CATEGORY_CREATE("CATEGORY_CREATE"),
    CATEGORY_UPDATE("CATEGORY_UPDATE"),
    CATEGORY_LIST("CATEGORY_LIST"),
    CATEGORY_GET("CATEGORY_GET"),
    CATEGORY_DELETE("CATEGORY_DELETE"),

    //SubCategory
    SUBCATEGORY_CREATE("SUBCATEGORY_CREATE"),
    SUBCATEGORY_UPDATE("SUBCATEGORY_UPDATE"),
    SUBCATEGORY_LIST("SUBCATEGORY_LIST"),
    SUBCATEGORY_GET("SUBCATEGORY_GET"),
    SUBCATEGORY_DELETE("SUBCATEGORY_DELETE"),

    //Discount
    DISCOUNT_CREATE("DISCOUNT_CREATE"),
    DISCOUNT_LIST("DISCOUNT_LIST"),
    DISCOUNT_GET("DISCOUNT_GET"),
    DISCOUNT_UPDATE("DISCOUNT_UPDATE"),
    DISCOUNT_DELETE("DISCOUNT_DELETE"),

    //Tax
    TAX_CREATE("TAX_CREATE"),
    TAX_UPDATE("TAX_UPDATE"),
    TAX_LIST("TAX_LIST"),
    TAX_DELETE("TAX_DELETE"),
    TAX_GET("TAX_GET"),

    //Service Fee
    SERVICE_FEE_CREATE("SERVICE_FEE_CREATE"),
    SERVICE_FEE_UPDATE("SERVICE_FEE_UPDATE"),
    SERVICE_FEE_LIST("SERVICE_FEE_LIST"),
    SERVICE_FEE_GET("SERVICE_FEE_GET"),
    SERVICE_FEE_DELETE("SERVICE_FEE_DELETE"),

    ;



    fun value() : String  = channel
}