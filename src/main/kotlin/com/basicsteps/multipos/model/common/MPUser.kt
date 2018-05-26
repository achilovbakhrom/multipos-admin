package com.basicsteps.multipos.model.common

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity
import de.braintags.io.vertx.pojomapper.annotation.field.Embedded


@Entity
open class MPUser(@Transient var keycloakId: String?,
                  @SerializedName("first_name") var firstName: String?,
                  @SerializedName("last_name") var lastName: String?,
                  @SerializedName("middle_name") var middleName: String?,
                  @SerializedName("birth_date") var birthDate: String?,
                  @SerializedName("gender") var gender: String?,
                  @SerializedName("nationality") var nationality: String?,
                  @SerializedName("marital_status") var maritalStatus: String?,
                  @SerializedName("spoken_languages") var spokenLanguages: List<String>?,
                  @Embedded @SerializedName("contact_data") var contactData: List<ContactData>?,
                  @Embedded @SerializedName("address_info") var addressInfo: AddressInformation?

) : BaseModel() {

    constructor(): this("", "", "", "", "", "", "", "", listOf(), listOf(), null)

    override fun instance(): Instanceable {
        val result = MPUser()

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
        result.keycloakId = keycloakId
        result.firstName = firstName
        result.lastName = lastName
        result.middleName = middleName
        result.birthDate = birthDate
        result.gender = gender
        result.nationality = nationality
        result.maritalStatus = maritalStatus
        result.spokenLanguages = spokenLanguages
        result.contactData = contactData
        result.addressInfo = addressInfo

        return result
    }

}

@Entity
data class ContactData(@SerializedName("type") var type: String?,
                       @SerializedName("data") var data: String?) : BaseModel () {
    constructor(): this("", "")

    override fun instance(): Instanceable {
        val result = ContactData()

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
        result.type = type
        result.data = data

        return result
    }
}

@Entity
data class AddressInformation(@SerializedName("name") var name: String?,
                              @SerializedName("type") var type: String?,
                              @SerializedName("street_address_1") var streetAddress1: String?,
                              @SerializedName("street_address_2") var streetAddress2: String?,
                              @SerializedName("city") var city: String?,
                              @SerializedName("country") var country: String?,
                              @SerializedName("state") var state: String?,
                              @SerializedName("post_code") var postCode: String?
) : BaseModel() {
    constructor(): this("", "", "","","","","","")

    override fun instance(): Instanceable {
        val result = AddressInformation()

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
        result.name = name
        result.type = type
        result.streetAddress1 = streetAddress1
        result.streetAddress2 = streetAddress2
        result.city = city
        result.country = country
        result.state = state
        result.postCode = postCode

        return result
    }
}

@Entity
data class PersonalIdentityInformation(@SerializedName("passport_number") var passportNumber: String?,
                                       @SerializedName("passport_issue_date") var passportIssueDate: String?,
                                       @SerializedName("passport_expire_date") var passportExpireDate: String?,
                                       @SerializedName("insurance_number") var insuranceNumber: String?,
                                       @SerializedName("SSN") var ssn: String?,
                                       @SerializedName("insurance_expire_date") var insuranceExpireDate: String?,
                                       @SerializedName("visaNumber") var visaNumber: String?,
                                       @SerializedName("visaIssueDate") var visaIssueDate: String?,
                                       @SerializedName("visa_expire_date") var visaExpireDate: String?,
                                       @SerializedName("wps_number") var wpsNumber: String?,
                                       @SerializedName("imageUrl") var imageUrl: String?
) : BaseModel() {
    constructor() : this("", "", "","","","","","","","","")
    override fun instance(): Instanceable {
        val result = PersonalIdentityInformation()

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
        result.passportNumber = passportNumber
        result.passportIssueDate = passportIssueDate
        result.passportExpireDate = passportExpireDate
        result.insuranceNumber = insuranceNumber
        result.ssn = ssn
        result.insuranceExpireDate = insuranceExpireDate
        result.visaNumber = visaNumber
        result.visaIssueDate = visaIssueDate
        result.visaExpireDate = visaExpireDate
        result.wpsNumber = wpsNumber
        result.imageUrl = imageUrl

        return result
    }
}