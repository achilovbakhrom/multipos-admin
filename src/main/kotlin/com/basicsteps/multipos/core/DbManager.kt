package com.basicsteps.multipos.core

import com.basicsteps.multipos.config.CommonConstants
import com.basicsteps.multipos.core.model.exceptions.TenantNotFoundException
import com.basicsteps.multipos.utils.DbConfig
import com.basicsteps.multipos.utils.KeycloakConfig
import com.basicsteps.multipos.worker.handling.dao.*
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore
import io.reactivex.Observable
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder


class DbManager(vertx: Vertx) {

    //daos
    var signUpDao: SignUpDao? = null
    var unitEntityDao: UnitEntityDao? = null // tenanstore unit dao
    var unitCategoryEntityDao: UnitCategoryEntityDao? = null //tenantstore unit category dao
    var currencyDao: CurrencyDao? = null
    var keycloak: Keycloak? = null // keycloak client
    var accountDao: AccountDao? = null
    var paymentTypeDao: PaymentTypeDao? = null
    var vendorDao: VendorDao? = null
    var productDao: ProductDao? = null
    var subCategoryDao: SubCategoryDao? = null
    var categoryDao: CategoryDao? = null
    var companyDao: CompanyDao? = null
    var posDao: POSDao? = null
    var establishmentDao: EstablishmentDao? = null
    var warehouseDao: WarehouseDao? = null
    var tenantsDao: TenantsDao? = null
    var userDao: UserDao? = null
    var userCompanyRelDao: UserCompanyRelDao? = null
    var warehouseVsEstablishmentDao: WarehouseVsEstablishmentDao? = null
    var productClassDao: ProductClassDao? = null
    var discountDao: DiscountDao? = null
    var taxDao: TaxDao? = null
    var serviceFeeDao: ServiceFeeDao? = null
    var mpUserDao: MPUserDao? = null
    var productToTaxDao: ProdoctToTaxDao? = null
    var customerDao: CustomerDao? = null
    var employeeDao: EmployeeDao? = null
    var orderDao: OrderDao? = null
    var customerGroupDao: CustomerGroupDao? = null
    var customerGroupVsCustomerDao: CustomerGroupVsCustomerDao? = null
    var invoiceDao: InvoiceDao? = null
    var incomingProductDao: IncomingProductDao? = null
    var warehouseQueueDao: WarehouseQueueDao? = null

    private var vertx: Vertx? = vertx

    init {
        //keycloak client
        this.keycloak = KeycloakBuilder.builder() //
                .serverUrl(KeycloakConfig.model.auth_endpoint) //
                .realm(KeycloakConfig.model.realm)//
                .username(KeycloakConfig.model.admin) //
                .password(KeycloakConfig.model.password) //
                .clientId(KeycloakConfig.model.tokenClientId) //
                .clientSecret(KeycloakConfig.model.tokenClientSecret)
                .resteasyClient(ResteasyClientBuilder().connectionPoolSize(10).build()) //
                .build()

        // init daos basing on datastores
        //sign up datastore
        signUpDao = SignUpDao(this, getSignUpDataStore())

        //common datastore
        companyDao = CompanyDao(this, getCommonDataStore())
        mpUserDao = MPUserDao(this, getCommonDataStore())
        userCompanyRelDao = UserCompanyRelDao(this, getCommonDataStore())
        tenantsDao = TenantsDao(this, getCommonDataStore())

        //tenant datastore
        unitEntityDao                   = UnitEntityDao(this, null)
        unitCategoryEntityDao           = UnitCategoryEntityDao(this, null)
        currencyDao                     = CurrencyDao(this, null)
        accountDao                      = AccountDao(this, null)
        paymentTypeDao                  = PaymentTypeDao(this, null)
        vendorDao                       = VendorDao(this, null)
        productDao                      = ProductDao(this, null)
        categoryDao                     = CategoryDao(this, null)
        subCategoryDao                  = SubCategoryDao(this, null)
        posDao                          = POSDao(this, null)
        establishmentDao                = EstablishmentDao(this, null)
        warehouseDao                    = WarehouseDao(this, null)
        warehouseVsEstablishmentDao     = WarehouseVsEstablishmentDao(this, null)
        productToTaxDao                 = ProdoctToTaxDao(this, null)
        productClassDao                 = ProductClassDao(this, null)
        discountDao                     = DiscountDao(this, null)
        taxDao                          = TaxDao(this, null)
        serviceFeeDao                   = ServiceFeeDao(this, null)
        employeeDao                     = EmployeeDao(this, null)
        customerDao                     = CustomerDao(this, null)
        orderDao                        = OrderDao(this, null)
        customerGroupDao                = CustomerGroupDao(this, null)
        customerGroupVsCustomerDao      = CustomerGroupVsCustomerDao(this, null)
        invoiceDao                      = InvoiceDao(this, null)
        incomingProductDao              = IncomingProductDao(this, null)
        warehouseQueueDao               = WarehouseQueueDao(this, null)
        //UserDao
        if (keycloak != null) userDao = UserDao(this)
        setMongoClientByTenantId(DbConfig.model.commonDbName)
    }

    var commonDatastore: MongoDataStore? = null
    private fun getCommonDataStore() : MongoDataStore? {
        if (commonDatastore == null) {
            val commonDatastoreConfig = JsonObject()
                    .put("connection_string", DbConfig.model.uri)
                    .put("db_name", DbConfig.model.commonDbName)
            commonDatastore = MongoDataStore(vertx, MongoClient.createNonShared(vertx, commonDatastoreConfig), commonDatastoreConfig)
        }
        return commonDatastore
    }
    
    var signUpdataStore: MongoDataStore? = null
    private fun getSignUpDataStore() : MongoDataStore? {
        if (signUpdataStore == null) {
            val signUpMongoConfig = JsonObject()
                    .put("connection_string", DbConfig.model.uri)
                    .put("db_name", DbConfig.model.signUpDbName)
            signUpdataStore = MongoDataStore(this.vertx, MongoClient.createNonShared(vertx, signUpMongoConfig), signUpMongoConfig)
        }
        return signUpdataStore
    }

    fun setMongoClientByTenantId(tenantId: String) {
        if (!tenantId.isNullOrEmpty()) {
            val tenantMongoConfig = JsonObject()
                    .put("connection_string", DbConfig.model.uri)
                    .put("db_name", tenantId)
            val temp = MongoDataStore(vertx, MongoClient.createNonShared(vertx, tenantMongoConfig), tenantMongoConfig)

            //set tenantStore to all daos
            unitEntityDao?.dataStore                = temp
            unitCategoryEntityDao?.dataStore        = temp
            currencyDao?.dataStore                  = temp
            accountDao?.dataStore                   = temp
            paymentTypeDao?.dataStore               = temp
            vendorDao?.dataStore                    = temp
            productDao?.dataStore                   = temp
            posDao?.dataStore                       = temp
            establishmentDao?.dataStore             = temp
            warehouseDao?.dataStore                 = temp
            warehouseVsEstablishmentDao?.dataStore  = temp
            productClassDao?.dataStore              = temp
            discountDao?.dataStore                  = temp
            taxDao?.dataStore                       = temp
            serviceFeeDao?.dataStore                = temp
            categoryDao?.dataStore                  = temp
            subCategoryDao?.dataStore               = temp
            productToTaxDao?.dataStore              = temp
            customerDao?.dataStore                  = temp
            employeeDao?.dataStore                  = temp
            orderDao?.dataStore                     = temp
            customerGroupDao?.dataStore             = temp
            customerGroupVsCustomerDao?.dataStore   = temp
            invoiceDao?.dataStore                   = temp
            incomingProductDao?.dataStore           = temp
            warehouseQueueDao?.dataStore            = temp
        }
    }
    fun close() { keycloak?.close() }

    fun setTenantId(tenantId: String) : Observable<Boolean> {
        return Observable.create({ event ->
            setMongoClientByTenantId(tenantId)
            event.onNext(true)
        })
    }

    fun getTenantIdByEmail(mail: String) : Observable<String> {
        return Observable.create({event ->
            val user = keycloak?.realm(KeycloakConfig.model.realm)?.users()?.search(mail)
            if (user != null && !user.isEmpty()) {
                val u = user.get(0)
                val attrs = u.attributes.get(CommonConstants.HEADER_TENANT)
                if (attrs != null && !attrs.isEmpty()) {
                    event.onNext(attrs.get(0))
                    return@create
                } else {
                    event.onError(TenantNotFoundException(mail))
                }

            } else {
                event.onError(TenantNotFoundException(mail))
            }
        })
    }

}