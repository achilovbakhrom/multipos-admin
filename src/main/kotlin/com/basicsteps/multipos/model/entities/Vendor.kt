package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.basicsteps.multipos.model.Contact
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity
import de.braintags.io.vertx.pojomapper.annotation.field.Embedded

@Entity
data class Vendor(@SerializedName("vendor_id") var vendorId: String,
                  @SerializedName("first_name") var firstName: String,
                  @SerializedName("last_name") var lastName: String,
                  @Embedded @SerializedName("contacts") var contacts: List<Person>,
                  @SerializedName("primary_phone") var primaryPhone: String,
                  @SerializedName("primary_email") var primaryEmail: String,
                  @SerializedName("address") var address: String) : BaseModel() {

    constructor(): this("","", "", listOf(), "", "", "")

    override fun instance(): Instanceable {
        val result = Vendor()

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

        //spec
        result.vendorId = vendorId
        result.firstName = firstName
        result.lastName = lastName
        result.contacts = contacts
        result.primaryEmail = primaryEmail
        result.primaryPhone = primaryPhone
        result.address = address

        return result
    }
}

@Entity
data class Person(@SerializedName("first_name") var firstName: String,
                  @SerializedName("last_name") var lastName: String,
                  @SerializedName("contact_list") var contactList: List<Contact>) : BaseModel() {

    constructor() : this("", "", listOf())
    override fun instance(): Instanceable {
        val result = Person()

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
        result.firstName = firstName
        result.lastName = lastName
        result.contactList = contactList

        return result
    }
}