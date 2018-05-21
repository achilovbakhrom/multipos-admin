package com.basicsteps.multipos.core.response

import com.basicsteps.multipos.core.model.Jsonable

data class DbResponse<T>(val t: T, val isError: Boolean, val msg: String) : Jsonable