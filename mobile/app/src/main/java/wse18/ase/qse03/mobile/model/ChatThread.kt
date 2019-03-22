package wse18.ase.qse03.mobile.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class ChatThread (
    var id: Long,
    var user: User,
    var office: Office?
): Serializable {

    constructor() : this(-1, User(), Office())
}