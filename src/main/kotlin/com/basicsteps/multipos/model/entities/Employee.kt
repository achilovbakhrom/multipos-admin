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
data class Employee(@Embedded @SerializedName("personal_identity_information") var personalIdentityInformation: PersonalIdentityInformation?,
                    @Embedded @SerializedName("employment_info") var employmentInformation: EmploymentInformation?) : MPUser() {

    constructor() : this(null, null)

    override fun instance(): Instanceable {
        val result = Employee()
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
        result.employmentInformation = employmentInformation

        return result
    }
}

@Entity
data class EmploymentInformation(@SerializedName("code") var code: String?,
                                 @SerializedName("department") var department: String?,
                                 @SerializedName("position") var position: String?,
                                 @Embedded @SerializedName("location") var location: Location?,
                                 @SerializedName("supervisor") var superVisor: String?,
                                 @SerializedName("hire_date") var hireDate: String?,
                                 @SerializedName("registration_date") var registrationDate: String?,
                                 @SerializedName("salary_grade") var salaryGrade: String?,
                                 @SerializedName("qualification") var qualification: String?,
                                 @SerializedName("skills") var skills: List<String>?,
                                 @SerializedName("hiring_document_number") var hiringDocumentNumber: String
) : BaseModel() {

    constructor() : this("", "", "", null, "", "", "", "", "", listOf(), "")

    override fun instance(): Instanceable {
        val result = EmploymentInformation()
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
        result.code = code
        result.department = department
        result.position = position
        result.location = location
        result.superVisor = superVisor
        result.hireDate = hireDate
        result.registrationDate = registrationDate
        result.salaryGrade = salaryGrade
        result.qualification = qualification
        result.skills = skills
        result.hiringDocumentNumber = hiringDocumentNumber

        return result
    }
}

@Entity
data class Location(@SerializedName("latitude") var latitude: String?,
                    @SerializedName("longitude") var longitude: String?) : BaseModel() {

    constructor() : this("", "")

    override fun instance(): Instanceable {
        val result = Location()
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
        result.latitude = latitude
        result.longitude = latitude

        return result
    }
}