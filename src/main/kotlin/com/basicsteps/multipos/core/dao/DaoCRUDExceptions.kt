package com.basicsteps.multipos.core.dao

class DataStoreException : Exception {
    constructor() : super("Datastore is null")
    constructor(message: String) : super(message)
}
