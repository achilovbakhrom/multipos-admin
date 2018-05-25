package com.basicsteps.multipos.worker.handling.handler.config

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.worker.handling.dao.ProductClassDao
import com.basicsteps.multipos.worker.handling.handler.company.CompanyHandler
import io.vertx.core.Vertx
import io.vertx.core.eventbus.Message

/**
 *  Config Handler includes couple of handlers
 *  List of Handlers:
 *      - unit category handler
 *      - unit handler
 *      - currency handler
 *      - account handler
 *      - payment type handler
 *      - vendor handler
 *      - product handler
 *      - category handler
 *      - subcategory handler
 *      - company handler
 *      - POS handler
 *      - establishment handler
 *
 *  Most of them are CRUD handlers
 */

class ConfigHandler(vertx: Vertx) {

    //handlers
    var unitCategoryHandler: UnitCategoryHandler? = null
    var unitHandler: UnitHandler? = null
    var currencyHandler: CurrencyHandler? = null
    var accountHandler: AccountHandler? = null
    var paymentTypeHandler: PaymentTypeHandler? = null
    var vendorHandler: VendorHandler? = null
    var productHandler: ProductHandler? = null
    var categoryHandler: CategoryHandler? = null
    var subCategoryHandler: SubCategoryHandler? = null
    var companyhandler: CompanyHandler? = null
    var posHandler: POSHandler? = null
    var establishmentHandler: EstablishmentHandler? = null
    var productClassHandler: ProductClassHandler? = null
    var discountHandler: DiscountHandler? = null
    var taxHandler: TaxHandler? = null
    var serviceFeeHandler: ServiceFeeHandler? = null
    var warehouseHandler: WarehouseHandler? = null
    /**
     *  init of all handlers
     */
    init {
        unitHandler = UnitHandler(vertx)
        unitCategoryHandler = UnitCategoryHandler(vertx)
        currencyHandler = CurrencyHandler(vertx)
        accountHandler = AccountHandler(vertx)
        paymentTypeHandler = PaymentTypeHandler(vertx)
        vendorHandler = VendorHandler(vertx)
        productHandler = ProductHandler(vertx)
        categoryHandler = CategoryHandler(vertx)
        subCategoryHandler = SubCategoryHandler(vertx)
        companyhandler = CompanyHandler(vertx)
        posHandler = POSHandler(vertx)
        establishmentHandler = EstablishmentHandler(vertx)
        productClassHandler = ProductClassHandler(vertx)
        discountHandler = DiscountHandler(vertx)
        taxHandler = TaxHandler(vertx)
        serviceFeeHandler = ServiceFeeHandler(vertx)
        warehouseHandler = WarehouseHandler(vertx)
    }

    /**
     *  Company crud
     */
    fun creatCompany(message: Message<String>) { companyhandler?.createCompany(message) }
    fun addUserToCompany(message: Message<String>) { companyhandler?.addUserToCompany(message) }
    fun trashCompany(message: Message<String>) { companyhandler?.trashCompany(message) }
    fun updateCompany(message: Message<String>) { companyhandler?.updateCompany(message) }
    fun getComaniesByUserId(message: Message<String>) { companyhandler?.getCompaniesByUserId(message) }


    /**
     *  Unit handlers
     */
    fun getUnitList(message: Message<String>) {unitHandler?.getUnitList(message)}
    fun getUnitById(message: Message<String>) {unitHandler?.getUnitById(message)}
    fun updateUnit(message: Message<String>) { unitHandler?.updateUnit(message) }

    /**
     *  Currency handlers
     */
    fun getCurrencies(message: Message<String>) { currencyHandler?.findAll(message) }
    fun getCurrencyById(message: Message<String>) { currencyHandler?.findById(message) }
    fun updateCurrency(message: Message<String>) { currencyHandler?.update(message) }

    /**
     *  Account handlers
     */
    fun getAccounts(message: Message<String>) {accountHandler?.getAccounts(message)}
    fun getAccountById(message: Message<String>) {accountHandler?.getAccountById(message)}
    fun updateAccount(message: Message<String>) {accountHandler?.updateAccount(message)}
    fun createAccount(message: Message<String>) { accountHandler?.createAccount(message) }
    fun deleteAccount(message: Message<String>) { accountHandler?.removeAccount(message) }

    /**
     *  Payment type handlers
     */
    fun createPaymentType(message: Message<String>) { paymentTypeHandler?.createPaymentType(message) }
    fun getPaymentTypeList(message: Message<String>) { paymentTypeHandler?.getPaymentTypeList(message) }
    fun updatePaymentType(message: Message<String>) { paymentTypeHandler?.updatePaymentType(message) }
    fun getPaymentTypeById(message: Message<String>) { paymentTypeHandler?.getPaymentTypeById(message)}
    fun deletePaymentType(message: Message<String>) { paymentTypeHandler?.deletePaymentType(message)}

    /**
     *  Vendors handlers
     */
    fun getVendors(message: Message<String>) { vendorHandler?.getVendors(message) }
    fun createVendor(message: Message<String>) { vendorHandler?.createVendor(message) }
    fun updateVendor(message: Message<String>) { vendorHandler?.updateVendor(message)}
    fun getVendorById(message: Message<String>) { vendorHandler?.getVendorById(message)}
    fun deleteVendor(message: Message<String>) { vendorHandler?.deleteVendor(message) }

    /**
     *  POS handlers
     */
    fun createPOS(message: Message<String>) { posHandler?.createPOS(message)}
    fun updatePOS(message: Message<String>) { posHandler?.updatePOS(message)}
    fun getPOSList(message: Message<String>) { posHandler?.getPOSList(message)}
    fun getPOSById(message: Message<String>) { posHandler?.getPOSById(message) }
    fun deletePOS(message: Message<String>) { posHandler?.trashPOS(message) }

    /**
     *  Warehouse handlers
     */
    fun createWarehouse(message: Message<String>) { warehouseHandler?.createWarehouse(message)}
    fun updateWarehouse(message: Message<String>) { warehouseHandler?.updateWarehouse(message)}
    fun getWarehouseList(message: Message<String>) { warehouseHandler?.getWarehouseList(message)}
    fun getWarehouseById(message: Message<String>) { warehouseHandler?.getWarehouseById(message) }
    fun deleteWarehouse(message: Message<String>) { warehouseHandler?.trashWarehouse(message) }

    /**
     *  Establishment handlers
     */
    fun createEstablishment(message: Message<String>) { establishmentHandler?.createEstablishment(message) }
    fun updateEstablishment(message: Message<String>) { establishmentHandler?.updateEstablishment(message) }
    fun getEstablishmentList(message: Message<String>) { establishmentHandler?.getEstablishmentList(message) }
    fun getEstablishmentById(message: Message<String>) { establishmentHandler?.getEstablishmentById(message) }
    fun deleteEstablishment(message: Message<String>) { establishmentHandler?.deleteEstablishment(message) }

    /**
     *  Product class handlers
     */
    fun createProductClass(message: Message<String>) { productClassHandler?.createProductClass(message) }
    fun updateProductClass(message: Message<String>) { productClassHandler?.updateProductClass(message) }
    fun getProductClassList(message: Message<String>) { productClassHandler?.getProductClassList(message) }
    fun getProductClassById(message: Message<String>) { productClassHandler?.getProductClassById(message) }
    fun deleteProductClass(message: Message<String>) { productClassHandler?.trashProductClassById(message) }

    /**
     *  Category handlers
     */
    fun createCategory(message: Message<String>) { categoryHandler?.createCategory(message) }
    fun updateCategory(message: Message<String>) { categoryHandler?.updateCategory(message) }
    fun getCategory(message: Message<String>) { categoryHandler?.getCategoryList(message) }
    fun getCategoryById(message: Message<String>) { categoryHandler?.getCategoryById(message) }
    fun deleteCategory(message: Message<String>) { categoryHandler?.trashCategoryById(message) }

    /**
     *  SubCategory handlers
     */
    fun createSubCategory(message: Message<String>) { subCategoryHandler?.createSubCategory(message) }
    fun updateSubCategory(message: Message<String>) { subCategoryHandler?.updateSubCategory(message) }
    fun getSubCategory(message: Message<String>) { subCategoryHandler?.getSubCategoryList(message) }
    fun getSubCategoryById(message: Message<String>) { subCategoryHandler?.getSubCategoryById(message) }
    fun deleteSubCategory(message: Message<String>) { subCategoryHandler?.trashSubCategoryById(message) }

    /**
     *  Discount
     */
    fun createDiscount(message: Message<String>) { discountHandler?.createDiscount(message) }
    fun updateDiscount(message: Message<String>) { discountHandler?.updateDiscount(message) }
    fun getDiscountList(message: Message<String>) { discountHandler?.getDiscountList(message) }
    fun getDiscountByid(message: Message<String>) { discountHandler?.getDiscountById(message) }
    fun deleteDiscount(message: Message<String>) { discountHandler?.deleteDiscount(message) }

    /**
     *  Tax
     */
    fun createTax(message: Message<String>) {
        taxHandler?.createTax(message)
    }

    fun updateTax(message: Message<String>) {
        taxHandler?.updateTax(message)
    }

    fun getTaxList(message: Message<String>) {
        taxHandler?.getTaxList(message)
    }

    fun getTaxById(message: Message<String>) {
        taxHandler?.getTaxById(message)
    }

    fun deleteTax(message: Message<String>) {
        taxHandler?.deleteTax(message)
    }

    /**
     *  Service Fee
     */
    fun createServiceFee(message: Message<String>) {
        serviceFeeHandler?.createServiceFee(message)
    }

    fun updateServiceFee(message: Message<String>) {
        serviceFeeHandler?.updateServiceFee(message)
    }

    fun getServiceFeeList(message: Message<String>) {
        serviceFeeHandler?.getServiceFeeList(message)
    }

    fun getServiceFeeById(message: Message<String>) {
        serviceFeeHandler?.getServiceFeeById(message)
    }

    fun deleteServiceFee(message: Message<String>) {
        serviceFeeHandler?.deleteServiceFee(message)
    }

    /**
     *  Product handlers
     */
    fun getProductList(message: Message<String>) { productHandler?.getProductList(message) }
    fun getProductById(message: Message<String>) {
        productHandler?.getProductById(message)
    }
    fun createProduct(message: Message<String>) {
        productHandler?.createProduct(message)
    }
    fun updateProduct(message: Message<String>) {
        productHandler?.updateProduct(message)
    }
    fun deleteProduct(message: Message<String>) {
        productHandler?.trashProductById(message)
    }

    /**
     *  Combo products handlers
     */
    fun getCombos(message: Message<String>) { TODO("get combos") }

    /**
     *  Matrix products handlers
     */
    fun getMatrices(message: Message<String>) { TODO("get matrices") }
}