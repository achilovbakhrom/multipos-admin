package com.basicsteps.multipos.model.common

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity


@Entity
data class MPUser(@Transient var keycloakId: String?,
                  @SerializedName("first_name") var firstName: String?,
                  @SerializedName("last_name") var lastName: String?,
                  @SerializedName("middle_name") var middleName: String?,
                  @SerializedName("birth_date") var birthDate: String?,
                  @SerializedName("gender") var gender: String?,
                  @SerializedName("nationality") var nationality: String?,
                  @SerializedName("marital_status") var maritalStatus: String?,
                  @SerializedName("spoken_languages") var spokenLanguages: List<String>?,
                  @SerializedName("contact_data") var contactData: List<ContactData>?,
                  @SerializedName("address_info") var addressInfo: AddressInformation?,
                  @SerializedName("personal_info") var personalInfo: PersonalIdentityInformation?,
                  @SerializedName("employment_info") var employmentInformation: EmploymentInformation?

) : BaseModel() {

    constructor(): this("", "", "", "", "", "", "", "", listOf(), listOf(), null, null, null)

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
        result.personalInfo = personalInfo
        result.employmentInformation = employmentInformation


        return result
    }

}

data class ContactData(@SerializedName("type") var type: String?,
                       @SerializedName("data") var data: String?)

data class AddressInformation(@SerializedName("name") var name: String?,
                              @SerializedName("type") var type: String?,
                              @SerializedName("street_address_1") var streetAddress1: String?,
                              @SerializedName("street_address_2") var streetAddress2: String?,
                              @SerializedName("city") var city: String?,
                              @SerializedName("country") var country: String?,
                              @SerializedName("state") var state: String?,
                              @SerializedName("post_code") var postCode: String?
)

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
)

data class EmploymentInformation(@SerializedName("code") var code: String?,
                                 @SerializedName("department") var department: String?,
                                 @SerializedName("position") var position: String?,
                                 @SerializedName("location") var location: Location?,
                                 @SerializedName("supervisor") var superVisor: String?,
                                 @SerializedName("hire_date") var hireDate: String?,
                                 @SerializedName("registration_date") var registrationDate: String?,
                                 @SerializedName("salary_grade") var salaryGrade: String?,
                                 @SerializedName("qualification") var qualification: String?,
                                 @SerializedName("skills") var skills: List<String>?
)

data class Location(@SerializedName("latitude") var latitude: String?,
                    @SerializedName("longitude") var longitude: String?)