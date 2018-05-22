package com.basicsteps.multipos.worker.handling

import com.basicsteps.multipos.config.CommonConstants
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.event_bus_channels.CompanyHandlerChannel
import com.basicsteps.multipos.event_bus_channels.ConfigHandlerChannel
import com.basicsteps.multipos.event_bus_channels.SignInHandlerChannel
import com.basicsteps.multipos.event_bus_channels.SignUpHandlerChannel
import com.basicsteps.multipos.worker.handling.handler.company.CompanyHandler
import com.basicsteps.multipos.worker.handling.handler.config.ConfigHandler
import com.basicsteps.multipos.worker.handling.handler.signIn.SignInHandler
import com.basicsteps.multipos.worker.handling.handler.signUp.SignUpHandler
import io.vertx.core.AbstractVerticle
import io.vertx.core.eventbus.Message

class WorkerVerticle : AbstractVerticle() {



    var signUpHandler: SignUpHandler? = null
    var signInHandler: SignInHandler? = null
    var configHandler: ConfigHandler? = null
    var companyHandler: CompanyHandler? = null

    override fun start() {
        super.start()
        initHandlers()
        initConsumers()
    }

    private fun initHandlers() {
        signUpHandler = SignUpHandler(vertx)
        signInHandler = SignInHandler(vertx)
        configHandler = ConfigHandler(vertx)
        companyHandler = CompanyHandler(vertx)
    }

    override fun stop() {
        super.stop()
    }

    private fun initConsumers() {

        //Tenant switcher
//        vertx.eventBus().consumer<String>(CommonConstants.SWITCH_TENANT, { message -> dbManager?.setMongoClientByTenantId(message.body().toString()) })

        //Sing Up
        registerHandler(SignUpHandlerChannel.SIGN_UP.value(), { signUpHandler?.signUp(it) })
        registerHandler(SignUpHandlerChannel.CONFIRM_ACCESS_CODE.value(), { signUpHandler?.handleConfirmation(it) })
        registerHandler(SignUpHandlerChannel.IS_EMAIL_UNIQUE.value(), { signUpHandler?.isEmailExists(it) })
        registerHandler(SignUpHandlerChannel.GET_ACCESS_CODE.value(), { signUpHandler?.getAccessCode(it) })

        //Sign In
        registerHandler(SignInHandlerChannel.SIGN_IN.value(), { signInHandler?.signInHandler(it) })
        registerHandler(SignInHandlerChannel.VERIFICATION.value(), { TODO("Verification handler") })
        registerHandler(SignInHandlerChannel.REFRESH_ACCESS_TOKEN.value(), { TODO("Refresh token handler") })
        registerHandler(SignInHandlerChannel.LOGOUT.value(), { TODO("Logout handler") })

        //company
        registerHandler(CompanyHandlerChannel.GET_COMPANIES_BY_USERNAME.value(), { companyHandler?.getCompaniesByUserId(it) })
        registerHandler(CompanyHandlerChannel.COMPANY_CREATE.value(), { companyHandler?.createCompany(it) })
        registerHandler(CompanyHandlerChannel.COMPANY_GET.value(), { companyHandler?.getCompanyById(it) })
        registerHandler(CompanyHandlerChannel.COMPANY_UPDATE.value(), { companyHandler?.updateCompany(it) })
        registerHandler(CompanyHandlerChannel.COMPANY_DELETE.value(), { companyHandler?.trashCompany(it) })

        //configuration
        //Product class
        registerHandler(ConfigHandlerChannel.PRODUCT_CLASS_CREATE.value(), { configHandler?.createProductClass(it) })
        registerHandler(ConfigHandlerChannel.PRODUCT_CLASS_UPDATE.value(), { configHandler?.updateProductClass(it) })
        registerHandler(ConfigHandlerChannel.PRODUCT_CLASS_LIST.value(), { configHandler?.getProductClassList(it) })
        registerHandler(ConfigHandlerChannel.PRODUCT_CLASS_GET.value(), { configHandler?.getProductClassById(it) })
        registerHandler(ConfigHandlerChannel.PRODUCT_CLASS_DELETE.value(), { configHandler?.deleteProductClass(it) })


        //Currency
        registerHandler(ConfigHandlerChannel.CURRENCY_LIST.value(), { configHandler?.getCurrencies(it) })
        registerHandler(ConfigHandlerChannel.CURRENCY_GET.value(), { configHandler?.getCurrencyById(it) })
        registerHandler(ConfigHandlerChannel.CURRENCY_UPDATE.value(), { configHandler?.updateCurrency(it) })

        //units
        registerHandler(ConfigHandlerChannel.UNIT_LIST.value(), { configHandler?.getUnitList(it) })
        registerHandler(ConfigHandlerChannel.UNIT_GET.value(), { configHandler?.getUnitById(it) })
        registerHandler(ConfigHandlerChannel.UNIT_UPDATE.value(), { configHandler?.updateUnit(it) })

        //Account
        registerHandler(ConfigHandlerChannel.ACCOUNT_CREATE.value(), { configHandler?.createAccount(it) })
        registerHandler(ConfigHandlerChannel.ACCOUNT_UPDATE.value(), { configHandler?.updateAccount(it) })
        registerHandler(ConfigHandlerChannel.ACCOUNT_GET.value(), { configHandler?.getAccountById(it) })
        registerHandler(ConfigHandlerChannel.ACCOUNT_LIST.value(), { configHandler?.getAccounts(it) })
        registerHandler(ConfigHandlerChannel.ACCOUNT_DELETE.value(), { configHandler?.deleteAccount(it) })

        //Payment type
        registerHandler(ConfigHandlerChannel.PAYMENT_TYPE_CREATE.value(), { configHandler?.createPaymentType(it) })
        registerHandler(ConfigHandlerChannel.PAYMENT_TYPE_LIST.value(), { configHandler?.getPaymentTypeList(it) })
        registerHandler(ConfigHandlerChannel.PAYMENT_TYPE_UPDATE.value(), { configHandler?.updatePaymentType(it) })
        registerHandler(ConfigHandlerChannel.PAYMENT_TYPE_GET.value(), { configHandler?.getPaymentTypeById(it) })
        registerHandler(ConfigHandlerChannel.PAYMENT_TYPE_DELETE.value(), { configHandler?.deletePaymentType(it) })

        //Vendor
        registerHandler(ConfigHandlerChannel.VENDOR_CREATE.value(), { configHandler?.createVendor(it) })
        registerHandler(ConfigHandlerChannel.VENDOR_LIST.value(), { configHandler?.getVendors(it) })
        registerHandler(ConfigHandlerChannel.VENDOR_UPDATE.value(), { configHandler?.updateVendor(it) })
        registerHandler(ConfigHandlerChannel.VENDOR_GET.value(), { configHandler?.getVendorById(it) })
        registerHandler(ConfigHandlerChannel.VENDOR_DELETE.value(), { configHandler?.deleteVendor(it) })

        //Product
        registerHandler(ConfigHandlerChannel.PRODUCT_LIST.value(), { configHandler?.getProductList(it) })
        registerHandler(ConfigHandlerChannel.PRODUCT_GET.value(), { configHandler?.getProductById(it) })
        registerHandler(ConfigHandlerChannel.PRODUCT_CREATE.value(), { configHandler?.createProduct(it) })
        registerHandler(ConfigHandlerChannel.PRODUCT_UPDATE.value(), { configHandler?.updateProduct(it) })
        registerHandler(ConfigHandlerChannel.PRODUCT_TRASH.value(), {configHandler?.trashCompany(it) })

        //POS & Stock
        registerHandler(ConfigHandlerChannel.POS_CREATE.value(), { configHandler?.createPOS(it) })
        registerHandler(ConfigHandlerChannel.POS_LIST.value(), { configHandler?.getPOSList(it) })
        registerHandler(ConfigHandlerChannel.POS_UPDATE.value(), { configHandler?.updatePOS(it) })
        registerHandler(ConfigHandlerChannel.POS_DELETE.value(), { configHandler?.deletePOS(it) })
        registerHandler(ConfigHandlerChannel.POS_GET.value(), { configHandler?.getPOSById(it) })

        registerHandler(ConfigHandlerChannel.STOCK_CREATE.value(), { configHandler?.createStock(it) })
        registerHandler(ConfigHandlerChannel.STOCK_LIST.value(), { configHandler?.getStockList(it) })
        registerHandler(ConfigHandlerChannel.STOCK_UPDATE.value(), { configHandler?.updateStock(it) })
        registerHandler(ConfigHandlerChannel.STOCK_DELETE.value(), { configHandler?.deleteStock(it) })
        registerHandler(ConfigHandlerChannel.STOCK_GET.value(), { configHandler?.getStockById(it) })

        //Establishment
        registerHandler(ConfigHandlerChannel.ESTABLISHMENT_CREATE.value(), { configHandler?.createEstablishment(it) })
        registerHandler(ConfigHandlerChannel.ESTABLISHMENT_UPDATE.value(), { configHandler?.updateEstablishment(it) })
        registerHandler(ConfigHandlerChannel.ESTABLISHMENT_LIST.value(), { configHandler?.getEstablishmentList(it) })
        registerHandler(ConfigHandlerChannel.ESTABLISHMENT_GET.value(), { configHandler?.getEstablishmentById(it) })
        registerHandler(ConfigHandlerChannel.ESTABLISHMENT_DELETE.value(), { configHandler?.deleteEstablishment(it) })

        //Discount
        registerHandler(ConfigHandlerChannel.DISCOUNT_CREATE.value(), { configHandler?.createDiscount(it) })
        registerHandler(ConfigHandlerChannel.DISCOUNT_UPDATE.value(), { configHandler?.updateDiscount(it) })
        registerHandler(ConfigHandlerChannel.DISCOUNT_LIST.value(), { configHandler?.getDiscountList(it) })
        registerHandler(ConfigHandlerChannel.DISCOUNT_GET.value(), { configHandler?.getDiscountByid(it) })
        registerHandler(ConfigHandlerChannel.DISCOUNT_DELETE.value(), { configHandler?.deleteDiscount(it) })

        //Tax
        registerHandler(ConfigHandlerChannel.TAX_CREATE.value(), { configHandler?.createTax(it) })
        registerHandler(ConfigHandlerChannel.TAX_UPDATE.value(), { configHandler?.updateTax(it) })
        registerHandler(ConfigHandlerChannel.TAX_DELETE.value(), { configHandler?.deleteTax(it) })
        registerHandler(ConfigHandlerChannel.TAX_GET.value(), { configHandler?.getTaxById(it) })
        registerHandler(ConfigHandlerChannel.TAX_LIST.value(), { configHandler?.getTaxList(it) })

        //Service Fee
        registerHandler(ConfigHandlerChannel.SERVICE_FEE_CREATE.value(), { configHandler?.createServiceFee(it)})
        registerHandler(ConfigHandlerChannel.SERVICE_FEE_UPDATE.value(), { configHandler?.updateServiceFee(it)})
        registerHandler(ConfigHandlerChannel.SERVICE_FEE_GET.value(), { configHandler?.getServiceFeeById(it)})
        registerHandler(ConfigHandlerChannel.SERVICE_FEE_LIST.value(), { configHandler?.getServiceFeeList(it)})
        registerHandler(ConfigHandlerChannel.SERVICE_FEE_DELETE.value(), { configHandler?.deleteServiceFee(it)})
    }

    private fun registerHandler(channelName: String, handler: (Message<String>) -> Unit) {
        vertx.eventBus().consumer<String>(channelName, { message -> handler(message)})
    }
}