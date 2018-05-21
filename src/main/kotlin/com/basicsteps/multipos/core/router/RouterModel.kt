package com.basicsteps.multipos.core.router

import com.basicsteps.multipos.core.model.Jsonable

class RouterModel : Jsonable {

    var id: String? = null
    var page: Int? = null
    var pageSize: Int? = null
    var ids: List<String>? = null


}