package com.basicsteps.multipos.core.model

import com.basicsteps.multipos.utils.JsonUtils

interface Trashable { var deleted: Boolean }

interface Activeable { var active: Boolean }

interface Auditable {
    var createdTime: Long
    var modifiedTime: Long
    var createdBy: String?
    var modifiedBy: String?
    var rootId: String?
    var posId: String?
}

interface Jsonable {
    fun toJson() : String = JsonUtils.toJson(this)
}

interface Instanceable {
    fun instance() : Instanceable
}

interface HasUserId {
    var userId: String?
}