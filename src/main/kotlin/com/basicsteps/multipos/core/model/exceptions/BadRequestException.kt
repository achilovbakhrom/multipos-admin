package com.basicsteps.multipos.core.model.exceptions

class BadRequestException(val text: String) : Exception("Bad request. Cause: $text") { constructor() : this("Unknown") }