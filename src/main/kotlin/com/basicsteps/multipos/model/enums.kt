package com.basicsteps.multipos.model

enum class GenderType(val type: String) {
    MALE("male"),
    FAMALE("famale");
    fun value() : String = type
}

enum class AccountType(val type: Int) { // Account types
    CASH(0),
    DEBT(1),
    CUSTOM(2);
    fun value() : Int = type
}

enum class DocumentType(val type: Int) {
    SALE(0),
    PURCHASE(1),
    VENDOR_DEBT_PAYMENTS(2);
    fun value() : Int = type
}

enum class OrderStatus(val type: Int) {
    OPEN(0),
    CLOSED(1),
    PENDING(2);
    fun value() : Int = type
}

enum class InvoiceType(val type: Int) {
    RECEIVE(0),
    RETURN(1);
    fun value() : Int = type
}

enum class InventoryOperation(val type: Int) {
    INVOICE(0),
    RETURN_OF_SALE(1),
    SURPLUS(2),
    SALE(3),
    RETURN_TO_VENDOR(4),
    WASTE(5);
    fun value() : Int = type
}

enum class QueueType(val type: Int) {
    FIFO(0),
    LIFO(1),
    FEFO(2);
    fun value() : Int = type
}

enum class ContactType(val type: Int) {
    PHONE(0),
    EMAIL(1),
    FACEBOOK(2);
    fun value() : Int = type
}

enum class UserType(val type: Int) {
    OWNER(0),
    PROVIDER(1),
    EMPLOYEE(2),
    OWNER_PROVIDER(3);
    fun value() : Int = type
}

enum class Languages(val language: String) {
    ENGLISH("en"),
    RUSSIAN("ru");
    fun value() : String = language
}

enum class KeycloakUserFields(val field: String) {
    LANGUAGE("language"),
    COMPANIES("companies");
    fun value() : String = field
}

enum class ErrorMessages(val message: String) {
    EMAIL_NOT_EXIST("Email not exists"),
    PASSWORD_IS_NOT_VALID("Password is not valid"),
    PHONE_NUMBER_IS_NOT_VALID("Phone number is not valid!!!"),
    EMAIL_IS_NOT_VALID("Email is not valid or empty!!!"),
    EMAIL_IS_EMPTY("Email is empty!!!"),
    EMAIL_IS_NOT_UNIQUE("Email already exists!!!"),
    DB_READ_ERROR("Error while read from DB!!!"),
    DB_WRITE_ERROR("Error while writing to DB!!!"),
    DB_DATASTORE_ERROR("Datastore is not set yet!!!"),
    WRONG_ACCESS_CODE("Wrong access code!!!"),
    COMPANY_NOT_FOUND("Company not found!!!"),
    UNKNOWN_ERROR("Unknown error"),
    COMPANY_IDENTIFIER_NOT_UNIQUE("Company identifier not unique!!!"),
    CANT_FIND_X_TENANT_ID_HEADER("Can't find X-TENANT-ID header!!!");
    fun value() : String = message
}


enum class UserAttrs(val field: String) {
    PRIMARY_PHONE("primaryPhone"),
    SEX("sex"),
    COUNTRY("country"),
    LANGUAGE("language"),
    CONTACT("contact"),
    IMAGE_URL("image_url"),
    COMPANY_IDS("companyIds"),
    BIRTHDAY("birthday");
    fun value() : String = field
}

enum class StatusMessages(val status: String) {
    ERROR("Error"),
    SUCCESS("Success");
    fun value() : String = status
}

enum class CompanyType(val type: Int) {
    B2C(0),
    B2B(1),
    BOTH(2);
    fun value(): Int = type
}

