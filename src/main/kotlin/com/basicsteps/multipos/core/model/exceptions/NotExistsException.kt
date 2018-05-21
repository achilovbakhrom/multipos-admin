package com.basicsteps.multipos.core.model.exceptions

class NotExistsException(column: String, value: String) : Exception("$value not found in database!!!")