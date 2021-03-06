package com.basicsteps.multipos.routing

import com.basicsteps.multipos.config.EndpointUriOperationId
import com.basicsteps.multipos.core.openApiCustomization.CustomOpenApiRouting
import com.basicsteps.multipos.core.putCorsAccesses
import com.basicsteps.multipos.event_bus_channels.SignUpHandlerChannel
import com.basicsteps.multipos.utils.KeycloakConfig
import com.basicsteps.multipos.utils.SystemConfig
import com.basicsteps.multipos.utils.logger
import com.basicsteps.multipos.routing.handler.CorrectAccessTokenHandler
import com.basicsteps.multipos.routing.handler.TenantSwitcherHandler
import com.basicsteps.multipos.routing.router.CompanyRouter
import com.basicsteps.multipos.routing.router.ConfigRouter
import com.basicsteps.multipos.routing.router.SignInRouter
import com.basicsteps.multipos.routing.router.SignUpRouter
import com.basicsteps.multipos.utils.getFromUrl
import io.vertx.core.AbstractVerticle
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerOptions
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.oauth2.OAuth2FlowType
import io.vertx.ext.auth.oauth2.providers.KeycloakAuth
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.CorsHandler
import io.vertx.ext.web.handler.impl.OAuth2AuthHandlerImpl

/**
 *  OpenApi configurations in the verticle, reads
 *  configurations from /src/main/resources/api.yaml
 */
class OpenApiRoutingVerticle : AbstractVerticle() {

    var server: HttpServer? = null
    var signUpRouter: SignUpRouter? = null
    var signInRouter: SignInRouter? = null
    var configRouter: ConfigRouter? = null
    var companyRouter: CompanyRouter? = null
    var tenantSwitcherHandler: TenantSwitcherHandler? = null
    var correctAccessTokenHandler: CorrectAccessTokenHandler? = null

    override fun start() {
        super.start()

        signUpRouter = SignUpRouter(vertx)
        signInRouter = SignInRouter(vertx)
        configRouter = ConfigRouter(vertx)
        companyRouter = CompanyRouter(vertx)

        tenantSwitcherHandler = TenantSwitcherHandler(vertx)
        correctAccessTokenHandler = CorrectAccessTokenHandler(vertx)

        val yamlString = getFromUrl(resourceName = "api.yaml")

        CustomOpenApiRouting.create(vertx, content = yamlString) { openAPI3RouterFactoryAsyncResult ->
            if (openAPI3RouterFactoryAsyncResult.succeeded()) {

                val routerFactory = openAPI3RouterFactoryAsyncResult.result()

                //Security setup
                val keycloakJson = JsonObject()
                        .put("realm", "master")
                        .put("realm-public-key", KeycloakConfig.model.publicKey)
                        .put("auth-server-url", KeycloakConfig.model.auth_endpoint)
                        .put("ssl-required", "external")
                        .put("resource", KeycloakConfig.model.bearerClientId)
                        .put("credentials", JsonObject()
                                .put("secret", KeycloakConfig.model.bearerClientSecret))

                val oauth2 = KeycloakAuth
                        .create(vertx, OAuth2FlowType.AUTH_CODE, keycloakJson)

                val handler = OAuth2AuthHandlerImpl(oauth2, SystemConfig.model.baseURL())
                        .setupCallback(Router.router(vertx).route("/api/v1/*"))

                routerFactory.addSecurityHandler(EndpointUriOperationId.OAUTH2.endpoint, handler)
                routerFactory.addSecurityHandler(EndpointUriOperationId.CORRECT_ACCESS_CODE_SECURITY.endpoint, { correctAccessTokenHandler?.isAccessCodeCorrect(it) })
                routerFactory.addSecurityHandler(EndpointUriOperationId.TENANT_SECURITY.endpoint, { tenantSwitcherHandler?.switchTenant(it) })
                routerFactory.router.route().handler(BodyHandler.create())

                //Sign In and Sign Up handlers
                registerHandler(routerFactory, EndpointUriOperationId.SIGN_UP, { signUpRouter?.signUp(it) })
                registerHandler(routerFactory, EndpointUriOperationId.SIGN_IN, { signInRouter?.signIn(it) })

                registerHandler(routerFactory, EndpointUriOperationId.CONFIRM_ACCESS_CODE, { signUpRouter?.confirmAccessCode(it) })
                registerHandler(routerFactory, EndpointUriOperationId.IS_EMAIL_EXISTS, { signUpRouter?.isEmailExists(it) })
                registerHandler(routerFactory, EndpointUriOperationId.GET_ACCESS_CODE, { signUpRouter?.getAccessCode(it) })
                registerHandler(routerFactory, EndpointUriOperationId.UPLOAD_AVATAR, { signUpRouter?.uploadAvatar(it) })
                registerHandler(routerFactory, EndpointUriOperationId.VERIFICATION, { signInRouter?.verification(it) })
                registerHandler(routerFactory, EndpointUriOperationId.LOGOUT, { signInRouter?.logout(it) })

                //Product class
                registerHandler(routerFactory, EndpointUriOperationId.PRODUCT_CLASS_CREATE, { configRouter?.createProductClass(it)})
                registerHandler(routerFactory, EndpointUriOperationId.PRODUCT_CLASS_UPDATE, { configRouter?.updateProductClass(it)})
                registerHandler(routerFactory, EndpointUriOperationId.PRODUCT_CLASS_LIST, { configRouter?.getProductClassList(it)})
                registerHandler(routerFactory, EndpointUriOperationId.PRODUCT_CLASS_GET, { configRouter?.getProductClassById(it)})
                registerHandler(routerFactory, EndpointUriOperationId.PRODUCT_CLASS_DELETE, { configRouter?.deleteProductClass(it)})


                //Config endpoints
                //Currency list
                registerHandler(routerFactory, EndpointUriOperationId.CURRENCY_LIST, {
                    configRouter?.getCurrencies(it)
                })
                registerHandler(routerFactory, EndpointUriOperationId.CURRENCY_GET, { configRouter?.getCurrencyById(it) })
                registerHandler(routerFactory, EndpointUriOperationId.CURRENCY_UPDATE, { configRouter?.updateCurrency(it) })

                //Company
                registerHandler(routerFactory, EndpointUriOperationId.COMPANY_CREATE, { companyRouter?.createCompany(it)})
                registerHandler(routerFactory, EndpointUriOperationId.COMPANY_LIST, { companyRouter?.getCompaniesByUsername(it)})
                registerHandler(routerFactory, EndpointUriOperationId.COMPANY_UPDATE, { companyRouter?.updateCompany(it)})
                registerHandler(routerFactory, EndpointUriOperationId.COMPANY_GET, { companyRouter?.getCompanyById(it)})
                registerHandler(routerFactory, EndpointUriOperationId.COMPANY_DELETE, { companyRouter?.deleteCompany(it)})

                //Category
                registerHandler(routerFactory, EndpointUriOperationId.CATEGORY_CREATE, { configRouter?.createCategory(it)})
                registerHandler(routerFactory, EndpointUriOperationId.CATEGORY_LIST, { configRouter?.getCategoryList(it)})
                registerHandler(routerFactory, EndpointUriOperationId.CATEGORY_UPDATE, { configRouter?.updateCategory(it)})
                registerHandler(routerFactory, EndpointUriOperationId.CATEGORY_GET, { configRouter?.getCategoryById(it)})
                registerHandler(routerFactory, EndpointUriOperationId.CATEGORY_DELETE, { configRouter?.deleteCategory(it)})

                //SubCategory
                registerHandler(routerFactory, EndpointUriOperationId.SUBCATEGORY_CREATE, { configRouter?.createSubCategory(it)})
                registerHandler(routerFactory, EndpointUriOperationId.SUBCATEGORY_LIST, { configRouter?.getSubCategoryList(it)})
                registerHandler(routerFactory, EndpointUriOperationId.SUBCATEGORY_UPDATE, { configRouter?.updateSubCategory(it)})
                registerHandler(routerFactory, EndpointUriOperationId.SUBCATEGORY_GET, { configRouter?.getSubCategoryById(it)})
                registerHandler(routerFactory, EndpointUriOperationId.SUBCATEGORY_DELETE, { configRouter?.deleteSubCategory(it)})

                //Product
                registerHandler(routerFactory, EndpointUriOperationId.PRODUCT_LIST, { configRouter?.getProductList(it) })
                registerHandler(routerFactory, EndpointUriOperationId.PRODUCT_GET, { configRouter?.getProductById(it) })
                registerHandler(routerFactory, EndpointUriOperationId.PRODUCT_CREATE, { configRouter?.createProduct(it) })
                registerHandler(routerFactory, EndpointUriOperationId.PRODUCT_UPDATE, { configRouter?.updateProduct(it) })
                registerHandler(routerFactory, EndpointUriOperationId.PRODUCT_DELETE, { configRouter?.removeProduct(it) })


                //POS & Warehouse
                registerHandler(routerFactory, EndpointUriOperationId.POS_CREATE, { configRouter?.createPOS(it) })
                registerHandler(routerFactory, EndpointUriOperationId.POS_LIST, { configRouter?.getPOSList(it) })
                registerHandler(routerFactory, EndpointUriOperationId.POS_UPDATE, { configRouter?.updatePOS(it) })
                registerHandler(routerFactory, EndpointUriOperationId.POS_GET, { configRouter?.getPOSById(it) })
                registerHandler(routerFactory, EndpointUriOperationId.POS_DELETE, { configRouter?.deletePOS(it) })

                registerHandler(routerFactory, EndpointUriOperationId.WAREHOUSE_CREATE, { configRouter?.createStock(it) })
                registerHandler(routerFactory, EndpointUriOperationId.WAREHOUSE_LIST, { configRouter?.stockList(it) })
                registerHandler(routerFactory, EndpointUriOperationId.WAREHOUSE_UPDATE, { configRouter?.updateStock(it) })
                registerHandler(routerFactory, EndpointUriOperationId.WAREHOUSE_GET, { configRouter?.getStockById(it) })
                registerHandler(routerFactory, EndpointUriOperationId.WAREHOUSE_DELETE, { configRouter?.deleteStock(it) })

                // establishment
                registerHandler(routerFactory, EndpointUriOperationId.ESTABLISHMENT_CREATE, { configRouter?.createEstablishment(it) })
                registerHandler(routerFactory, EndpointUriOperationId.ESTABLISHMENT_UPDATE, { configRouter?.updateEstablishment(it) })
                registerHandler(routerFactory, EndpointUriOperationId.ESTABLISHMENT_LIST, { configRouter?.establishmentList(it) })
                registerHandler(routerFactory, EndpointUriOperationId.ESTABLISHMENT_GET, { configRouter?.getEstablishmentById(it) })
                registerHandler(routerFactory, EndpointUriOperationId.ESTABLISHMENT_DELETE, { configRouter?.deleteEstablishment(it) })

                //units
                registerHandler(routerFactory, EndpointUriOperationId.UNIT_LIST, { configRouter?.getUnitList(it) })
                registerHandler(routerFactory, EndpointUriOperationId.UNIT_GET, { configRouter?.getUnitById(it) })
                registerHandler(routerFactory, EndpointUriOperationId.UNIT_UPDATE, { configRouter?.updateUnit(it) })

                //Account
                registerHandler(routerFactory, EndpointUriOperationId.ACCOUNT_CREATE, { configRouter?.createAccount(it) })
                registerHandler(routerFactory, EndpointUriOperationId.ACCOUNT_GET, { configRouter?.getAccountById(it) })
                registerHandler(routerFactory, EndpointUriOperationId.ACCOUNT_UPDATE, { configRouter?.updateAccount(it) })
                registerHandler(routerFactory, EndpointUriOperationId.ACCOUNT_LIST, { configRouter?.getAccountList(it) })
                registerHandler(routerFactory, EndpointUriOperationId.ACCOUNT_DELETE, { configRouter?.deleteAccount(it) })

                //PaymentType
                registerHandler(routerFactory, EndpointUriOperationId.PAYMENT_TYPE_CREATE, { configRouter?.createPaymentType(it) })
                registerHandler(routerFactory, EndpointUriOperationId.PAYMENT_TYPE_LIST, { configRouter?.getPaymentTypeList(it) })
                registerHandler(routerFactory, EndpointUriOperationId.PAYMENT_TYPE_UPDATE, { configRouter?.updatePaymentType(it) })
                registerHandler(routerFactory, EndpointUriOperationId.PAYMENT_TYPE_GET, { configRouter?.getPaymentTypeById(it) })
                registerHandler(routerFactory, EndpointUriOperationId.PAYMENT_TYPE_DELETE, { configRouter?.deletePaymentType(it) })

                //Vendor
                registerHandler(routerFactory, EndpointUriOperationId.VENDOR_CREATE, { configRouter?.createVendor(it) })
                registerHandler(routerFactory, EndpointUriOperationId.VENDOR_LIST, { configRouter?.getVendorList(it) })
                registerHandler(routerFactory, EndpointUriOperationId.VENDOR_UPDATE, { configRouter?.updateVendor(it) })
                registerHandler(routerFactory, EndpointUriOperationId.VENDOR_DELETE, { configRouter?.deleteVendor(it) })
                registerHandler(routerFactory, EndpointUriOperationId.VENDOR_GET, { configRouter?.getVendorById(it) })

                //Discount
                registerHandler(routerFactory, EndpointUriOperationId.DISCOUNT_CREATE, { configRouter?.createDiscount(it) })
                registerHandler(routerFactory, EndpointUriOperationId.DISCOUNT_UPDATE, { configRouter?.updateDiscount(it) })
                registerHandler(routerFactory, EndpointUriOperationId.DISCOUNT_LIST, { configRouter?.getDiscountList(it) })
                registerHandler(routerFactory, EndpointUriOperationId.DISCOUNT_GET, { configRouter?.getDiscountById(it) })
                registerHandler(routerFactory, EndpointUriOperationId.DISCOUNT_DELETE, { configRouter?.deleteDiscount(it) })

                //Tax
                registerHandler(routerFactory, EndpointUriOperationId.TAX_CREATE, { configRouter?.createTax(it) })
                registerHandler(routerFactory, EndpointUriOperationId.TAX_UPDATE, { configRouter?.updateTax(it) })
                registerHandler(routerFactory, EndpointUriOperationId.TAX_LIST, { configRouter?.getTaxList(it) })
                registerHandler(routerFactory, EndpointUriOperationId.TAX_GET, { configRouter?.getTaxById(it) })
                registerHandler(routerFactory, EndpointUriOperationId.TAX_DELETE, { configRouter?.deleteTax(it) })

                //Service Fee
                registerHandler(routerFactory, EndpointUriOperationId.SERVICE_FEE_CREATE, { configRouter?.createServiceFee(it) })
                registerHandler(routerFactory, EndpointUriOperationId.SERVICE_FEE_UPDATE, { configRouter?.updateServiceFee(it) })
                registerHandler(routerFactory, EndpointUriOperationId.SERVICE_FEE_LIST, { configRouter?.getServiceFeeList(it) })
                registerHandler(routerFactory, EndpointUriOperationId.SERVICE_FEE_GET, { configRouter?.getServiceFeeById(it) })
                registerHandler(routerFactory, EndpointUriOperationId.SERVICE_FEE_DELETE, { configRouter?.deleteServiceFee(it) })

                //Customer
                registerHandler(routerFactory, EndpointUriOperationId.CUSTOMER_CREATE, { configRouter?.createCustomer(it) })
                registerHandler(routerFactory, EndpointUriOperationId.CUSTOMER_UPDATE, { configRouter?.updateCustomer(it) })
                registerHandler(routerFactory, EndpointUriOperationId.CUSTOMER_LIST, { configRouter?.getCustomerList(it) })
                registerHandler(routerFactory, EndpointUriOperationId.CUSTOMER_GET, { configRouter?.getCustomerById(it) })
                registerHandler(routerFactory, EndpointUriOperationId.CUSTOMER_DELETE, { configRouter?.deleteCustomer(it) })

                //Employee
                registerHandler(routerFactory, EndpointUriOperationId.EMPLOYEE_CREATE, { configRouter?.createEmployee(it) })
                registerHandler(routerFactory, EndpointUriOperationId.EMPLOYEE_UPDATE, { configRouter?.updateEmployee(it) })
                registerHandler(routerFactory, EndpointUriOperationId.EMPLOYEE_LIST, { configRouter?.getEmployeeList(it) })
                registerHandler(routerFactory, EndpointUriOperationId.EMPLOYEE_GET, { configRouter?.getEmployeeById(it) })
                registerHandler(routerFactory, EndpointUriOperationId.EMPLOYEE_DELETE, { configRouter?.deleteEmployee(it) })

                //Order
                registerHandler(routerFactory, EndpointUriOperationId.ORDER_CREATE, { configRouter?.createOrder(it) })
                registerHandler(routerFactory, EndpointUriOperationId.ORDER_UPDATE, { configRouter?.updateOrder(it) })
                registerHandler(routerFactory, EndpointUriOperationId.ORDER_LIST, { configRouter?.getOrderList(it) })
                registerHandler(routerFactory, EndpointUriOperationId.ORDER_GET, { configRouter?.getOrderById(it) })
                registerHandler(routerFactory, EndpointUriOperationId.ORDER_DELETE, { configRouter?.deleteOrder(it) })

                //Customer Group
                registerHandler(routerFactory, EndpointUriOperationId.CUSTOMER_GROUP_CREATE, { configRouter?.createCustomerGroup(it) })
                registerHandler(routerFactory, EndpointUriOperationId.CUSTOMER_GROUP_UPDATE, { configRouter?.updateCustomerGroup(it) })
                registerHandler(routerFactory, EndpointUriOperationId.CUSTOMER_GROUP_LIST, { configRouter?.getCustomerGroupList(it) })
                registerHandler(routerFactory, EndpointUriOperationId.CUSTOMER_GROUP_GET, { configRouter?.getCustomerGroupById(it) })
                registerHandler(routerFactory, EndpointUriOperationId.CUSTOMER_GROUP_DELETE, { configRouter?.deleteCustomerGroup(it) })

                //Invoice
                registerHandler(routerFactory, EndpointUriOperationId.INVOICE_CREATE, { configRouter?.createInvoice(it) })
                registerHandler(routerFactory, EndpointUriOperationId.INVOICE_UPDATE, { configRouter?.updateInvoice(it) })
                registerHandler(routerFactory, EndpointUriOperationId.INVOICE_LIST, { configRouter?.getInvoiceList(it) })
                registerHandler(routerFactory, EndpointUriOperationId.INVOICE_GET, { configRouter?.getInvoiceById(it) })
                registerHandler(routerFactory, EndpointUriOperationId.INVOICE_DELETE, { configRouter?.deleteInvoice(it) })

                //Inventory
                registerHandler(routerFactory, EndpointUriOperationId.RETURN_FROM_CUSTOMER, { configRouter?.returnFromCustomer(it) })
                registerHandler(routerFactory, EndpointUriOperationId.RETORN_TO_VENDOR, { configRouter?.returnToVendor(it) })
                registerHandler(routerFactory, EndpointUriOperationId.SURPLUS, { configRouter?.surplus(it) })
                registerHandler(routerFactory, EndpointUriOperationId.WASTE, { configRouter?.waste(it) })


                //Exchange
                registerHandler(routerFactory, EndpointUriOperationId.EXCHANGE_CREATE, { configRouter?.createExchange(it) })
                registerHandler(routerFactory, EndpointUriOperationId.EXCHANGE_UPDATE, { configRouter?.updateExchange(it) })
                registerHandler(routerFactory, EndpointUriOperationId.EXCHANGE_GET, { configRouter?.getExchangeById(it) })
                registerHandler(routerFactory, EndpointUriOperationId.EXCHANGE_LIST, { configRouter?.getExchangeList(it) })
                registerHandler(routerFactory, EndpointUriOperationId.EXCHANGE_DELETE, { configRouter?.deleteExchange(it) })

                //CORS accesses
                val router = routerFactory.router
                router.route().handler(CorsHandler.create("*").putCorsAccesses())

                //open api is loaded success
                server = vertx.createHttpServer(HttpServerOptions().setPort(SystemConfig.model.port.toInt()).setHost(SystemConfig.model.host))
                server?.requestHandler(router::accept)?.listen()
                logger.info("Server is run")
            } else {
                logger.error("OpenApiRoutingError! ${openAPI3RouterFactoryAsyncResult.cause()}")
            }
        }

//        OpenAPI3RouterFactory.create(vertx, "src/main/resources/api.yaml") { openAPI3RouterFactoryAsyncResult ->
//            if (openAPI3RouterFactoryAsyncResult.succeeded()) {
//
//                val routerFactory = openAPI3RouterFactoryAsyncResult.result()
//
//                //Security setup
//                val keycloakJson = JsonObject()
//                        .put("realm", "master")
//                        .put("realm-public-key", KeycloakConfig.model.publicKey)
//                        .put("auth-server-url", KeycloakConfig.model.auth_endpoint)
//                        .put("ssl-required", "external")
//                        .put("resource", KeycloakConfig.model.bearerClientId)
//                        .put("credentials", JsonObject()
//                                .put("secret", KeycloakConfig.model.bearerClientSecret))
//
//                val oauth2 = KeycloakAuth
//                        .create(vertx, OAuth2FlowType.AUTH_CODE, keycloakJson)
//
//                val handler = OAuth2AuthHandlerImpl(oauth2, SystemConfig.model.baseURL())
//                        .setupCallback(Router.router(vertx).route("/api/v1/*"))
//
//                routerFactory.addSecurityHandler(EndpointUriOperationId.OAUTH2.endpoint, handler)
//                routerFactory.addSecurityHandler(EndpointUriOperationId.CORRECT_ACCESS_CODE_SECURITY.endpoint, { correctAccessTokenHandler?.isAccessCodeCorrect(it) })
//                routerFactory.addSecurityHandler(EndpointUriOperationId.TENANT_SECURITY.endpoint, { tenantSwitcherHandler?.switchTenant(it) })
//                routerFactory.router.route().handler(BodyHandler.create())
//
//                //Sign In and Sign Up handlers
//                registerHandler(routerFactory, EndpointUriOperationId.SIGN_UP, { signUpRouter?.routeSave(it, SignUpHandlerChannel.SIGN_UP.value()) })
//                registerHandler(routerFactory, EndpointUriOperationId.SIGN_IN, { signInRouter?.signIn(it) })
//
//                registerHandler(routerFactory, EndpointUriOperationId.CONFIRM_ACCESS_CODE, { signUpRouter?.confirmAccessCode(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.IS_EMAIL_EXISTS, { signUpRouter?.isEmailExists(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.GET_ACCESS_CODE, { signUpRouter?.getAccessCode(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.UPLOAD_AVATAR, { signUpRouter?.uploadAvatar(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.VERIFICATION, { signInRouter?.verification(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.LOGOUT, { signInRouter?.logout(it) })
//
//                //Product class
//                registerHandler(routerFactory, EndpointUriOperationId.PRODUCT_CLASS_CREATE, { configRouter?.createProductClass(it)})
//                registerHandler(routerFactory, EndpointUriOperationId.PRODUCT_CLASS_UPDATE, { configRouter?.updateProductClass(it)})
//                registerHandler(routerFactory, EndpointUriOperationId.PRODUCT_CLASS_LIST, { configRouter?.getProductClassList(it)})
//                registerHandler(routerFactory, EndpointUriOperationId.PRODUCT_CLASS_GET, { configRouter?.getProductClassById(it)})
//                registerHandler(routerFactory, EndpointUriOperationId.PRODUCT_CLASS_DELETE, { configRouter?.deleteProductClass(it)})
//
//
//                //Config endpoints
//                //Currency list
//                registerHandler(routerFactory, EndpointUriOperationId.CURRENCY_LIST, {
//                    configRouter?.getCurrencies(it)
//                })
//                registerHandler(routerFactory, EndpointUriOperationId.CURRENCY_GET, { configRouter?.getCurrencyById(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.CURRENCY_UPDATE, { configRouter?.updateCurrency(it) })
//
//                //Company
//                registerHandler(routerFactory, EndpointUriOperationId.COMPANY_CREATE, { companyRouter?.createCompany(it)})
//                registerHandler(routerFactory, EndpointUriOperationId.COMPANY_LIST, { companyRouter?.getCompaniesByUsername(it)})
//                registerHandler(routerFactory, EndpointUriOperationId.COMPANY_UPDATE, { companyRouter?.updateCompany(it)})
//                registerHandler(routerFactory, EndpointUriOperationId.COMPANY_GET, { companyRouter?.getCompanyById(it)})
//                registerHandler(routerFactory, EndpointUriOperationId.COMPANY_DELETE, { companyRouter?.deleteCompany(it)})
//
//                //Product
//                registerHandler(routerFactory, EndpointUriOperationId.PRODUCT_LIST, { configRouter?.getProductList(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.PRODUCT_GET, { configRouter?.getProductById(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.PRODUCT_CREATE, { configRouter?.createProduct(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.PRODUCT_UPDATE, { configRouter?.updateProduct(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.PRODUCT_DELETE, { configRouter?.removeProduct(it) })
//
//
//                //POS & Warehouse
//                registerHandler(routerFactory, EndpointUriOperationId.POS_CREATE, { configRouter?.createPOS(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.POS_LIST, { configRouter?.getPOSList(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.POS_UPDATE, { configRouter?.updatePOS(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.POS_GET, { configRouter?.getPOSById(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.POS_DELETE, { configRouter?.deletePOS(it) })
//
//                registerHandler(routerFactory, EndpointUriOperationId.WAREHOUSE_CREATE, { configRouter?.createStock(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.WAREHOUSE_LIST, { configRouter?.stockList(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.WAREHOUSE_UPDATE, { configRouter?.updateStock(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.WAREHOUSE_GET, { configRouter?.getStockById(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.WAREHOUSE_DELETE, { configRouter?.deleteStock(it) })
//
//
//                //units
//                registerHandler(routerFactory, EndpointUriOperationId.UNIT_LIST, { configRouter?.getUnitList(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.UNIT_GET, { configRouter?.getUnitById(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.UNIT_UPDATE, { configRouter?.updateUnit(it) })
//
//                //Account
//                registerHandler(routerFactory, EndpointUriOperationId.ACCOUNT_CREATE, { configRouter?.createAccount(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.ACCOUNT_GET, { configRouter?.getAccountById(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.ACCOUNT_UPDATE, { configRouter?.updateAccount(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.ACCOUNT_LIST, { configRouter?.getAccountList(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.ACCOUNT_DELETE, { configRouter?.deleteAccount(it) })
//
//                //PaymentType
//                registerHandler(routerFactory, EndpointUriOperationId.PAYMENT_TYPE_CREATE, { configRouter?.createPaymentType(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.PAYMENT_TYPE_LIST, { configRouter?.getPaymentTypeList(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.PAYMENT_TYPE_UPDATE, { configRouter?.updatePaymentType(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.PAYMENT_TYPE_GET, { configRouter?.getPaymentTypeById(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.PAYMENT_TYPE_DELETE, { configRouter?.deletePaymentType(it) })
//
//                //Vendor
//                registerHandler(routerFactory, EndpointUriOperationId.VENDOR_CREATE, { configRouter?.createVendor(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.VENDOR_LIST, { configRouter?.getVendorList(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.VENDOR_UPDATE, { configRouter?.updateVendor(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.VENDOR_DELETE, { configRouter?.deleteVendor(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.VENDOR_GET, { configRouter?.getVendorById(it) })
//
//                //Discount
//                registerHandler(routerFactory, EndpointUriOperationId.DISCOUNT_CREATE, { configRouter?.createDiscount(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.DISCOUNT_UPDATE, { configRouter?.updateDiscount(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.DISCOUNT_LIST, { configRouter?.getDiscountList(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.DISCOUNT_GET, { configRouter?.getDiscountById(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.DISCOUNT_DELETE, { configRouter?.deleteDiscount(it) })
//
//                //Tax
//                registerHandler(routerFactory, EndpointUriOperationId.TAX_CREATE, { configRouter?.createTax(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.TAX_UPDATE, { configRouter?.updateTax(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.TAX_LIST, { configRouter?.getTaxList(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.TAX_GET, { configRouter?.getTaxById(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.TAX_DELETE, { configRouter?.deleteTax(it) })
//
//                //Service Fee
//                registerHandler(routerFactory, EndpointUriOperationId.SERVICE_FEE_CREATE, { configRouter?.createServiceFee(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.SERVICE_FEE_UPDATE, { configRouter?.updateServiceFee(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.SERVICE_FEE_LIST, { configRouter?.getServiceFeeList(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.SERVICE_FEE_GET, { configRouter?.getServiceFeeById(it) })
//                registerHandler(routerFactory, EndpointUriOperationId.SERVICE_FEE_DELETE, { configRouter?.deleteServiceFee(it) })
//
//                //CORS accesses
//                val router = routerFactory.router
//                router.route().handler(CorsHandler.create("*").putCorsAccesses())
//
//                //open api is loaded success
//                server = vertx.createHttpServer(HttpServerOptions().setPort(SystemConfig.model.port.toInt()).setHost(SystemConfig.model.host))
//                server?.requestHandler(router::accept)?.listen()
//                logger.info("Server is run")
//            } else {
//                logger.error("OpenApiRoutingError! ${openAPI3RouterFactoryAsyncResult.cause()}")
//            }
//        }
    }

    fun registerHandler(factory: OpenAPI3RouterFactory, operationId: EndpointUriOperationId, handler: (routingContext: RoutingContext) -> Unit) {
        factory.addHandlerByOperationId(operationId.endpoint, { routingContext: RoutingContext -> handler(routingContext) })
    }


    override fun stop() {
        super.stop()
        server?.close()
    }
}