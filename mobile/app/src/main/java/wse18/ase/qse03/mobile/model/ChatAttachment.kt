package wse18.ase.qse03.mobile.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


@JsonIgnoreProperties(ignoreUnknown = true)
data class ChatAttachment(
    var fileName: String,
    var fileType: String
) {

    constructor() : this("", "")
}