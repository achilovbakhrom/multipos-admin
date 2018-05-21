package com.basicsteps.multipos.routing.router

import com.basicsteps.multipos.core.router.BaseRouter
import com.basicsteps.multipos.core.router.RouteBuilder
import com.basicsteps.multipos.event_bus_channels.ConfigHandlerChannel
import com.basicsteps.multipos.model.entities.*
import com.basicsteps.multipos.utils.RoutingUtils
import io.vertx.core.Vertx
import io.vertx.ext.web.RoutingContext
import io.vertx.reactivex.ext.web.Route

/**
 * Configuration:
 *
 *  - Units
 *  - Unit categories
 *  - Currency
 *  - Account
 *  - Payment type
 *  - Vendor
 *  - Product
 *  - Combo
 *  - Matrix
 *
 */

class ConfigRouter(vertx: Vertx): BaseRouter(vertx) {

    /**
     *  POS CRUD routes
     */
    fun createPOS(routingContext: RoutingContext) {
        RouteBuilder().post().handle(vertx, routingContext, ConfigHandlerChannel.POS_CREATE.value())
    }
    fun getPOSList(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.POS_LIST.value())
    }
    fun getPOSById(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.POS_GET.value())
    }
    fun updatePOS(routingContext: RoutingContext) {
        RouteBuilder().put().handle(vertx, routingContext, ConfigHandlerChannel.POS_UPDATE.value())
    }
    fun deletePOS(routingContext: RoutingContext) {
        RouteBuilder().delete().handle(vertx, routingContext, ConfigHandlerChannel.POS_DELETE.value())
    }

    /**
     *  Stock CRUD routes
     */
    fun createStock(routingContext: RoutingContext) {
        RouteBuilder().post().handle(vertx, routingContext, ConfigHandlerChannel.STOCK_CREATE.value())
    }
    fun stockList(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.STOCK_LIST.value())
    }
    fun getStockById(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.STOCK_GET.value())
    }
    fun updateStock(routingContext: RoutingContext) {
        RouteBuilder().put().handle(vertx, routingContext, ConfigHandlerChannel.STOCK_UPDATE.value())
    }
    fun deleteStock(routingContext: RoutingContext) {
        RouteBuilder().delete().handle(vertx, routingContext, ConfigHandlerChannel.STOCK_DELETE.value())
    }

    /**
     *  Unit routes
     */
    fun getUnitList(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.UNIT_LIST.value())
    }

    fun getUnitById(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.UNIT_GET.value())
    }

    fun updateUnit(routingContext: RoutingContext) {
        RouteBuilder().put().handle(vertx, routingContext, ConfigHandlerChannel.UNIT_UPDATE.value())
    }
    /**
     *  Currency routes
     */
    fun getCurrencies(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.CURRENCY_LIST.value())
    }

    fun getCurrencyById(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.CURRENCY_GET.value())
    }

    fun updateCurrency(routingContext: RoutingContext) {
        RouteBuilder().put().handle(vertx, routingContext, ConfigHandlerChannel.CURRENCY_UPDATE.value())
    }

    /**
     *  Account routes
     */
    fun getAccountList(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.ACCOUNT_LIST.value())
    }
    fun createAccount(routingContext: RoutingContext) {
        RouteBuilder().post().handle(vertx, routingContext, ConfigHandlerChannel.ACCOUNT_CREATE.value())
    }

    fun updateAccount(routingContext: RoutingContext) {
        RouteBuilder().put().handle(vertx, routingContext, ConfigHandlerChannel.ACCOUNT_UPDATE.value())
    }

    fun getAccountById(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.ACCOUNT_GET.value())
    }

    fun deleteAccount(routingContext: RoutingContext) {
        RouteBuilder().delete().handle(vertx, routingContext, ConfigHandlerChannel.ACCOUNT_DELETE.value())
    }


    /**
     *  Payment type routes
     */
    fun createPaymentType(routingContext: RoutingContext) {
        RouteBuilder().post().handle(vertx, routingContext, ConfigHandlerChannel.PAYMENT_TYPE_CREATE.value())
    }

    fun updatePaymentType(routingContext: RoutingContext) {
        RouteBuilder().put().handle(vertx, routingContext, ConfigHandlerChannel.PAYMENT_TYPE_UPDATE.value())
    }

    fun getPaymentTypeList(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.PAYMENT_TYPE_LIST.value())
    }

    fun getPaymentTypeById(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.PAYMENT_TYPE_GET.value())
    }

    fun deletePaymentType(routingContext: RoutingContext) {
        RouteBuilder().delete().handle(vertx, routingContext, ConfigHandlerChannel.PAYMENT_TYPE_DELETE.value())
    }



    /**
     *  Vendor routes
     */
    fun createVendor(routingContext: RoutingContext) {
        RouteBuilder().post().handle(vertx, routingContext, ConfigHandlerChannel.VENDOR_CREATE.value())
    }

    fun updateVendor(routingContext: RoutingContext) {
        RouteBuilder().put().handle(vertx, routingContext, ConfigHandlerChannel.VENDOR_UPDATE.value())
    }

    fun getVendorList(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.VENDOR_LIST.value())
    }

    fun getVendorById(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.VENDOR_GET.value())
    }

    fun deleteVendor(routingContext: RoutingContext) {
        RouteBuilder().delete().handle(vertx, routingContext, ConfigHandlerChannel.VENDOR_DELETE.value())
    }

    /**
     *  Product routes
     */
    fun createProduct(routingContext: RoutingContext) {
        RouteBuilder().post().handle(vertx, routingContext, ConfigHandlerChannel.PRODUCT_CREATE.value())
    }

    fun updateProduct(routingContext: RoutingContext) {
        RouteBuilder().put().handle(vertx, routingContext, ConfigHandlerChannel.PRODUCT_UPDATE.value())
    }

    fun getProductList(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.PRODUCT_LIST.value())
    }

    fun getProductById(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.PRODUCT_GET.value())
    }

    fun removeProduct(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.PRODUCT_TRASH.value())
    }

    /**
     *  Product class routes
     */
    fun createProductClass(routingContext: RoutingContext) {
        RouteBuilder().post().handle(vertx, routingContext, ConfigHandlerChannel.PRODUCT_CLASS_CREATE.value())
    }

    fun getProductClassList(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.PRODUCT_CLASS_LIST.value())
    }

    fun updateProductClass(routingContext: RoutingContext) {
        RouteBuilder().put().handle(vertx, routingContext, ConfigHandlerChannel.PRODUCT_CLASS_UPDATE.value())
    }

    fun getProductClassById(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.PRODUCT_CLASS_GET.value())
    }

    fun deleteProductClass(routingContext: RoutingContext) {
        RouteBuilder().delete().handle(vertx, routingContext, ConfigHandlerChannel.PRODUCT_CLASS_DELETE.value())
    }

    /**
     *  Discount routes
     */
    fun createDiscount(routingContext: RoutingContext) {
        RouteBuilder().post().handle(vertx, routingContext, ConfigHandlerChannel.DISCOUNT_CREATE.value())
    }

    fun updateDiscount(routingContext: RoutingContext) {
        RouteBuilder().put().handle(vertx, routingContext, ConfigHandlerChannel.DISCOUNT_UPDATE.value())
    }

    fun getDiscountList(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.DISCOUNT_LIST.value())
    }

    fun getDiscountById(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.DISCOUNT_GET.value())
    }

    fun deleteDiscount(routingContext: RoutingContext) {
        RouteBuilder().delete().handle(vertx, routingContext, ConfigHandlerChannel.DISCOUNT_DELETE.value())
    }

    /**
     *  Tax
     */
    fun createTax(routingContext: RoutingContext) {
        RouteBuilder().post().handle(vertx, routingContext, ConfigHandlerChannel.TAX_CREATE.value())
    }

    fun updateTax(routingContext: RoutingContext) {
        RouteBuilder().put().handle(vertx, routingContext, ConfigHandlerChannel.TAX_UPDATE.value())
    }

    fun getTaxList(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.TAX_LIST.value())
    }

    fun getTaxById(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.TAX_GET.value())
    }

    fun deleteTax(routingContext: RoutingContext) {
        RouteBuilder().delete().handle(vertx, routingContext, ConfigHandlerChannel.TAX_DELETE.value())
    }

    /**
     *  Service Fee
     */
    fun createServiceFee(routingContext: RoutingContext) {
        RouteBuilder().post().handle(vertx, routingContext, ConfigHandlerChannel.SERVICE_FEE_CREATE.value())
    }

    fun updateServiceFee(routingContext: RoutingContext) {
        RouteBuilder().put().handle(vertx, routingContext, ConfigHandlerChannel.SERVICE_FEE_UPDATE.value())
    }

    fun getServiceFeeList(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.SERVICE_FEE_LIST.value())
    }

    fun getServiceFeeById(routingContext: RoutingContext) {
        RouteBuilder().get().handle(vertx, routingContext, ConfigHandlerChannel.SERVICE_FEE_GET.value())
    }

    fun deleteServiceFee(routingContext: RoutingContext) {
        RouteBuilder().delete().handle(vertx, routingContext, ConfigHandlerChannel.SERVICE_FEE_DELETE.value())
    }


    // ---------------- Combo routes --------------------
    fun createComboProduct(routingContext: RoutingContext) {

    }

    fun updateComboProduct(routingContext: RoutingContext) {

    }

    fun getAllComboProducts(routingContext: RoutingContext) {

    }

    fun getComboProductById(routingContext: RoutingContext) {

    }

    fun removeComboProduct(routingContext: RoutingContext) {

    }

    fun removeComboProducts(routingContext: RoutingContext) {

    }

    fun trashComboProduct(routingContext: RoutingContext) {

    }

    fun trashComboProducts(routingContext: RoutingContext) {

    }
    // ---------------- End Combo routes ----------------

    // ---------------- Combo routes --------------------
    fun createMatrix(routingContext: RoutingContext) {

    }

    fun updateMatrix(routingContext: RoutingContext) {

    }

    fun getAllMatrix(routingContext: RoutingContext) {

    }

    fun getMatrixById(routingContext: RoutingContext) {

    }

    fun removeMatrix(routingContext: RoutingContext) {

    }

    fun removeMatrices(routingContext: RoutingContext) {

    }

    fun trashMatrix(routingContext: RoutingContext) {

    }

    fun trashMatrices(routingContext: RoutingContext) {

    }
    // ---------------- End Combo routes ----------------

}