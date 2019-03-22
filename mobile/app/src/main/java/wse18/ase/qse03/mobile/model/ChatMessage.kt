package wse18.ase.qse03.mobile.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class ChatMessage (
    var id: Long,
    var chatThread: ChatThread,
    var createDateTime: Date,
    var message: String,
    var chatAttachment: ChatAttachment?,
    var patientMessage: Boolean,
    var senderUid: String,
    var chatAttachmentPresent: Boolean
): Serializable {
    constructor() : this(-1, ChatThread(), Date(), "", null,  true, "", false)
}