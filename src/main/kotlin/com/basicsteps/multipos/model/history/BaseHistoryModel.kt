package com.basicsteps.multipos.model.history

import com.basicsteps.multipos.core.model.Jsonable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.field.Id
import java.io.Serializable

enum class OperationActionsType(var action: String) {
    CREATE("CREATE"), UPDATE("UPDATE"), DELETE("DELETE");
    fun value() : String = action
}

abstract class BaseHistoryModel: Jsonable, Serializable {
    @Id var id: String? = null
    @SerializedName("time") var date: String? = null
    @SerializedName("action") var action: String? = null
    @SerializedName("source") var source: String? = null
    @SerializedName("destination") var destination: String? = null
}