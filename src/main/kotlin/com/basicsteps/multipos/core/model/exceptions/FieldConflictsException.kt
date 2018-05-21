package com.basicsteps.multipos.core.model.exceptions

class FieldConflictsException(val field: String, val value: String) : Exception("Such field exists: $field: $value")