package wse18.ase.qse03.mobile.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable

/**
 * User corresponds to Patient on backend!
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class User(
    var id: Long,
    var uid: String,
    var svnr: String?,
    var preTitle: String?,
    var firstName: String?,
    var lastName: String?,
    var posTitle: String?,
    var eMail: String,
    var privateKey: String?,
    var publicKey: String?
): Serializable {

    constructor() : this(-1, "", "", "", "", "", "", "", "", "")
}