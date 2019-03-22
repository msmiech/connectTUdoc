package wse18.ase.qse03.mobile.ui.myDoctors

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import wse18.ase.qse03.mobile.model.ChatMessage
import wse18.ase.qse03.mobile.model.ChatThread
import wse18.ase.qse03.mobile.repository.ChatRepo
import android.util.Log
import com.virgilsecurity.android.ethree.kotlin.interaction.EThree
import wse18.ase.qse03.mobile.model.Medicine
import wse18.ase.qse03.mobile.repository.MedicineRepo
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


class ChatViewModel : ViewModel() {

    var chatLD: MutableLiveData<ChatThread> = MutableLiveData()
    var messageListLD: MutableLiveData<List<ChatMessage>> = MutableLiveData()
    var sentMessageLD: MutableLiveData<ChatMessage> = MutableLiveData()
    var medicine: MutableLiveData<Medicine> = MutableLiveData()
    lateinit var chatRepo: ChatRepo
    lateinit var medicineRepo:  MedicineRepo
    lateinit var scheduleTaskExecutor: ScheduledExecutorService


    fun init() {
        chatRepo = ChatRepo()
        medicineRepo = MedicineRepo()
        scheduleTaskExecutor = Executors.newScheduledThreadPool(5)
    }

    fun getChatByOfficeId(idToken: String?, officeId: Long) {
        chatRepo.getChatByOfficeId(chatLD, idToken, officeId)
    }

    fun getMessagesByChatId(idToken: String?, eThree: EThree, receiverUIDs: List<String>, threadId: Long) {
        chatRepo.getAllChatMessages(messageListLD, idToken, eThree, receiverUIDs, threadId)
    }

    fun startGettingMessagesByChatId(idToken: String?, eThree: EThree, receiverUIDs: List<String>, threadId: Long) {
        scheduleTaskExecutor.scheduleAtFixedRate(Runnable {
            chatRepo.getAllChatMessages(messageListLD, idToken, eThree, receiverUIDs, threadId)
        }, 0, 1, TimeUnit.SECONDS)
    }

    fun stopGettingMessages() {
        scheduleTaskExecutor.shutdown() // Disable new tasks from being submitted
        try {
            if (!scheduleTaskExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduleTaskExecutor.shutdownNow()
                if (!scheduleTaskExecutor.awaitTermination(10, TimeUnit.SECONDS))
                    Log.wtf("ChatViewModel", "scheduleTaskExecutor did not terminate!")
            }
        } catch (ie: InterruptedException) {
            scheduleTaskExecutor.shutdownNow()
            Thread.currentThread().interrupt()
        }

    }

    fun sendChatMessage(idToken: String?, eThree: EThree, receiverUIDs: List<String>, threadId: Long, message: String) {
        chatRepo.createChatMessage(sentMessageLD, idToken, eThree, receiverUIDs, threadId, message)
    }

    fun sendChatMessageWithAttachment(idToken: String?, eThree: EThree, receiverUIDs: List<String>, threadId: Long, message: String, file: File) {
        if (!file.exists()) {
            Log.wtf("ChatViewModel", "sendChatMessageWithAttachment - file does not exist!")
            return
        }

        chatRepo.createChatMessageWithAttachment(sentMessageLD, idToken, eThree, receiverUIDs, threadId, message, file)
    }

	fun getMedicineFromBarcode(idToken: String?, id: String?)  {
        val medicine = medicineRepo.getMedicineForId(medicine,idToken,id)
        Log.i("ChatViewModelMedicine", "Response body string: " + medicine)
    }

}