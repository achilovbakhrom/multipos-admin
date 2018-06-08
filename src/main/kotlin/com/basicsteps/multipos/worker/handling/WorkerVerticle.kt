package com.basicsteps.multipos.worker.handling

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

        //Category
        registerHandler(ConfigHandlerChannel.CATEGORY_CREATE.value(), {configHandler?.createCategory(it)})
        registerHandler(ConfigHandlerChannel.CATEGORY_UPDATE.value(), {configHandler?.updateCategory(it)})
        registerHandler(ConfigHandlerChannel.CATEGORY_LIST.value(), {configHandler?.getCategory(it)})
        registerHandler(ConfigHandlerChannel.CATEGORY_GET.value(), {configHandler?.getCategoryById(it)})
        registerHandler(ConfigHandlerChannel.CATEGORY_DELETE.value(), {configHandler?.deleteCategory(it)})

        //SubCategory
        registerHandler(ConfigHandlerChannel.SUBCATEGORY_CREATE.value(), {configHandler?.createSubCategory(it)})
        registerHandler(ConfigHandlerChannel.SUBCATEGORY_UPDATE.value(), {configHandler?.updateSubCategory(it)})
        registerHandler(ConfigHandlerChannel.SUBCATEGORY_LIST.value(), {configHandler?.getSubCategory(it)})
        registerHandler(ConfigHandlerChannel.SUBCATEGORY_GET.value(), {configHandler?.getSubCategoryById(it)})
        registerHandler(ConfigHandlerChannel.SUBCATEGORY_DELETE.value(), {configHandler?.deleteSubCategory(it)})

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

        //POS & Warehouse
        registerHandler(ConfigHandlerChannel.POS_CREATE.value(), { configHandler?.createPOS(it) })
        registerHandler(ConfigHandlerChannel.POS_LIST.value(), { configHandler?.getPOSList(it) })
        registerHandler(ConfigHandlerChannel.POS_UPDATE.value(), { configHandler?.updatePOS(it) })
        registerHandler(ConfigHandlerChannel.POS_DELETE.value(), { configHandler?.deletePOS(it) })
        registerHandler(ConfigHandlerChannel.POS_GET.value(), { configHandler?.getPOSById(it) })

        registerHandler(ConfigHandlerChannel.WAREHOUSE_CREATE.value(), { configHandler?.createWarehouse(it) })
        registerHandler(ConfigHandlerChannel.WAREHOUSE_LIST.value(), { configHandler?.getWarehouseList(it) })
        registerHandler(ConfigHandlerChannel.WAREHOUSE_UPDATE.value(), { configHandler?.updateWarehouse(it) })
        registerHandler(ConfigHandlerChannel.WAREHOUSE_DELETE.value(), { configHandler?.deleteWarehouse(it) })
        registerHandler(ConfigHandlerChannel.WAREHOUSE_GET.value(), { configHandler?.getWarehouseById(it) })

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

        //Customer
        registerHandler(ConfigHandlerChannel.CUSTOMER_CREATE.value(), { configHandler?.createCustomer(it)})
        registerHandler(ConfigHandlerChannel.CUSTOMER_UPDATE.value(), { configHandler?.updateCustomer(it)})
        registerHandler(ConfigHandlerChannel.CUSTOMER_GET.value(), { configHandler?.getCustomerById(it)})
        registerHandler(ConfigHandlerChannel.CUSTOMER_LIST.value(), { configHandler?.getCustomerList(it)})
        registerHandler(ConfigHandlerChannel.CUSTOMER_DELETE.value(), { configHandler?.deleteCustomer(it)})

        //Customer
        registerHandler(ConfigHandlerChannel.EMPLOYEE_CREATE.value(), { configHandler?.createEmployee(it)})
        registerHandler(ConfigHandlerChannel.EMPLOYEE_UPDATE.value(), { configHandler?.updateEmployee(it)})
        registerHandler(ConfigHandlerChannel.EMPLOYEE_GET.value(), { configHandler?.getEmployeeById(it)})
        registerHandler(ConfigHandlerChannel.EMPLOYEE_LIST.value(), { configHandler?.getEmployeeList(it)})
        registerHandler(ConfigHandlerChannel.EMPLOYEE_DELETE.value(), { configHandler?.deleteEmployee(it)})

        //Order
        registerHandler(ConfigHandlerChannel.ORDER_CREATE.value(), { configHandler?.createOrder(it)})
        registerHandler(ConfigHandlerChannel.ORDER_UPDATE.value(), { configHandler?.updateOrder(it)})
        registerHandler(ConfigHandlerChannel.ORDER_GET.value(), { configHandler?.getOrderById(it)})
        registerHandler(ConfigHandlerChannel.ORDER_LIST.value(), { configHandler?.getOrderList(it)})
        registerHandler(ConfigHandlerChannel.ORDER_DELETE.value(), { configHandler?.deleteOrder(it)})

        //Customer Group
        registerHandler(ConfigHandlerChannel.CUSTOMER_GROUP_CREATE.value(), { configHandler?.createCustomerGroup(it)})
        registerHandler(ConfigHandlerChannel.CUSTOMER_GROUP_UPDATE.value(), { configHandler?.updateCustomerGroup(it)})
        registerHandler(ConfigHandlerChannel.CUSTOMER_GROUP_GET.value(), { configHandler?.getCustomerGroupById(it)})
        registerHandler(ConfigHandlerChannel.CUSTOMER_GROUP_LIST.value(), { configHandler?.getCustomerGroupList(it)})
        registerHandler(ConfigHandlerChannel.CUSTOMER_GROUP_DELETE.value(), { configHandler?.deleteCustomerGroup(it)})

        //Invoice
        registerHandler(ConfigHandlerChannel.INVOICE_CREATE.value(), { configHandler?.createInvoice(it)})
        registerHandler(ConfigHandlerChannel.INVOICE_UPDATE.value(), { configHandler?.updateInvoice(it)})
        registerHandler(ConfigHandlerChannel.INVOICE_GET.value(), { configHandler?.getInvoiceById(it)})
        registerHandler(ConfigHandlerChannel.INVOICE_LIST.value(), { configHandler?.getInvoiceList(it)})
        registerHandler(ConfigHandlerChannel.INVOICE_DELETE.value(), { configHandler?.deleteInvoice(it)})

        //Inventory
        registerHandler(ConfigHandlerChannel.CHANGE_PRODUCT_COUNT_INVENTORY.value(), { configHandler?.changeProductCountInventory(it)})

        //Exchange
        registerHandler(ConfigHandlerChannel.ESTABLISHMENT_CREATE.value(), { configHandler?.createExchange(it) })
        registerHandler(ConfigHandlerChannel.ESTABLISHMENT_UPDATE.value(), { configHandler?.updateExchange(it) })
        registerHandler(ConfigHandlerChannel.ESTABLISHMENT_GET.value(), { configHandler?.getExchangeById(it) })
        registerHandler(ConfigHandlerChannel.ESTABLISHMENT_LIST.value(), { configHandler?.getExchangeList(it) })
        registerHandler(ConfigHandlerChannel.ESTABLISHMENT_DELETE.value(), { configHandler?.deleteExchange(it) })
    }

    private fun registerHandler(channelName: String, handler: (Message<String>) -> Unit) {
        vertx.eventBus().consumer<String>(channelName, { message -> handler(message)})
    }
}