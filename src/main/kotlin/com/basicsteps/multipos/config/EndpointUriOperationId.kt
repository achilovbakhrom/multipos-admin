package com.basicsteps.multipos.config

enum class EndpointUriOperationId(val endpoint: String) {

    //sign in & sign up
    SIGN_UP("sign-up"),
    SIGN_IN("sign-in"),
    CONFIRM_ACCESS_CODE("confirm"),
    IS_EMAIL_EXISTS("check-email"),
    GET_ACCESS_CODE("access-code"),
    VERIFICATION("verification"),
    UPLOAD_AVATAR("upload-avatar"),
    LOGOUT("logout"),

    //units
    UNIT_LIST("unit-list"),
    UNIT_UPDATE("unit-update"),
    UNIT_GET("unit-get"),

    //currency
    CURRENCY_LIST("currency-list"),
    CURRENCY_GET("currency-get"),
    CURRENCY_UPDATE("currency-update"),

    //units
    DEACTIVATE_UNIT("deactivate-unit"),
    ACTIVATE_UNIT("activate-unit"),

    //account
    ACCOUNT_CREATE("account-create"),
    ACCOUNT_LIST("account-list"),
    ACCOUNT_UPDATE("account-update"),
    ACCOUNT_GET("account-get"),
    ACCOUNT_DELETE("account-delete"),

    //payment type
    PAYMENT_TYPE_CREATE("payment-type-create"),
    PAYMENT_TYPE_LIST("payment-type-list"),
    PAYMENT_TYPE_UPDATE("payment-type-update"),
    PAYMENT_TYPE_GET("payment-type-get"),
    PAYMENT_TYPE_DELETE("payment-type-delete"),

    //vendor
    VENDOR_CREATE("vendor-create"),
    VENDOR_LIST("vendor-list"),
    VENDOR_GET("vendor-get"),
    VENDOR_UPDATE("vendor-update"),
    VENDOR_DELETE("vendor-delete"),

    //pos
    POS_LIST("pos-list"),
    POS_UPDATE("pos-update"),
    POS_CREATE("pos-create"),
    POS_GET("pos-get"),
    POS_DELETE("pos-delete"),

    //stock
    STOCK_CREATE("stock-create"),
    STOCK_LIST("stock-list"),
    STOCK_UPDATE("stock-update"),
    STOCK_GET("stock-get"),
    STOCK_DELETE("stock-delete"),

    //company
    COMPANY_CREATE("company-create"),
    COMPANY_LIST("company-list"),
    COMPANY_UPDATE("company-update"),
    COMPANY_GET("company-get"),
    COMPANY_DELETE("company-delete"),

    PRODUCT_LIST("product-list"),
    PRODUCT_GET("product-get"),
    PRODUCT_CREATE("product-create"),
    PRODUCT_UPDATE("product-update"),
    PRODUCT_DELETE("product-delete"),

    //Product class
    PRODUCT_CLASS_LIST("product-class-list"),
    PRODUCT_CLASS_CREATE("product-class-create"),
    PRODUCT_CLASS_UPDATE("product-class-update"),
    PRODUCT_CLASS_GET("product-class-get"),
    PRODUCT_CLASS_DELETE("product-class-delete"),

    //security
    OAUTH2("OAuth2"),
    CORRECT_ACCESS_CODE_SECURITY("CorrectAccessCodeSecurity"),
    TENANT_SECURITY("TenantSecurity"),

    //Discount
    DISCOUNT_CREATE("discount-create"),
    DISCOUNT_LIST("discount-list"),
    DISCOUNT_GET("discount-get"),
    DISCOUNT_UPDATE("discount-update"),
    DISCOUNT_DELETE("discount-delete"),

    //Tax
    TAX_CREATE("tax-create"),
    TAX_UPDATE("tax-update"),
    TAX_LIST("tax-list"),
    TAX_GET("tax-get"),
    TAX_DELETE("tax-delete"),

    SERVICE_FEE_CREATE("service-fee-create"),
    SERVICE_FEE_UPDATE("service-fee-update"),
    SERVICE_FEE_LIST("service-fee-list"),
    SERVICE_FEE_GET("service-fee-get"),
    SERVICE_FEE_DELETE("service-fee-delete"),


}