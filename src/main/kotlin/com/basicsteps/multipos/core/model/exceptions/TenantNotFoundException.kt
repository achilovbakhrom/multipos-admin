package com.basicsteps.multipos.core.model.exceptions

class TenantNotFoundException(mail: String) : Exception("Can\'t find tenantId for $mail")