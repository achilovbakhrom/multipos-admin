package com.basicsteps.multipos.model

import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity
import de.braintags.io.vertx.pojomapper.annotation.field.Id

@Entity
data class Contact(@SerializedName("id") @Id var id: String,
                   @SerializedName("type") var type: ContactType,
                   @SerializedName("data") var data: String) {
    constructor() : this("", ContactType.EMAIL, "")
}