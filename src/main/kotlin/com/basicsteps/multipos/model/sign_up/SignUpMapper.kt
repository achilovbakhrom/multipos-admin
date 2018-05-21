package com.basicsteps.multipos.model.sign_up

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity


@Entity
data class SignUpMapper(@SerializedName("mail") var mail: String,
                        @SerializedName("password") var password: String,
                        @SerializedName("access_code") var accessCode: Int) : BaseModel() {

    override fun instance(): Instanceable {
        val result = SignUpMapper()
        //base
        result.id = id
        result.createdTime = createdTime
        result.modifiedTime = modifiedTime
        result.createdBy = createdBy
        result.modifiedBy = id
        result.active = active
        result.userId = userId
        result.deleted = deleted
        result.rootId = rootId
        result.modifiedId = modifiedId
        result.posId = posId

        //specs
        result.mail = mail
        result.password = password
        result.accessCode = accessCode

        return result
    }

    constructor() : this("", "", -1)

}