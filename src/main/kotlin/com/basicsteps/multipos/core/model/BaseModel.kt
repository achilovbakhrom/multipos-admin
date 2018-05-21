package com.basicsteps.multipos.core.model

import com.basicsteps.multipos.core.DbManager
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.field.Id
import de.braintags.io.vertx.pojomapper.annotation.lifecycle.BeforeSave
import java.io.Serializable
import java.util.*

abstract class BaseModel : Trashable, Activeable, Auditable, Jsonable, Instanceable, Serializable, HasUserId {

    @Id @SerializedName("id") var id: String? = null
    @Transient override var createdTime = -1L
    @Transient override var modifiedTime = -1L
    @Transient override var createdBy: String? = null
    @Transient override var modifiedBy: String? = null
    @SerializedName("active") override var active: Boolean = false
    @Transient override var userId: String? = null
    @Transient override var deleted: Boolean = false
    @Transient override var rootId: String? = null
    @Transient var modifiedId: String? = null
    @Transient override var posId: String? = null
    @Transient var access: String? = null
    @Transient var dbManager: DbManager? = null

    @BeforeSave
    fun beforeSave() {
        val time = Date().time
        if (createdTime == - 1L) {
            createdTime = time
            createdBy = userId
        }
        modifiedTime = time
        modifiedBy = userId
    }
}

enum class ModelScope(val type: String) {
    PRIVATE("private"),
    PUBLIC("public"),
    PER_ESTABLISHMENT("per-establishment"),
    PER_COMPANY("per-company");
    fun value() : String = type
}