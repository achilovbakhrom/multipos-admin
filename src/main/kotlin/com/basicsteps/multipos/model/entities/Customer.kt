package com.basicsteps.multipos.model.entities

/**
 * Created by Ikrom Mirzayev on 26-May-18.
 */
import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.basicsteps.multipos.model.common.MPUser
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity
import de.braintags.io.vertx.pojomapper.annotation.field.Embedded
import de.braintags.io.vertx.pojomapper.annotation.field.Id

@Entity
data class Customer(@Embedded @SerializedName("personal_identity_information") var personalIdentityInformation: PersonalIdentityInformation?) : MPUser() {

    constructor() : this(null)

    override fun instance(): Instanceable {
        val result = Customer()
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

        //specifications
        result.personalIdentityInformation = personalIdentityInformation

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
