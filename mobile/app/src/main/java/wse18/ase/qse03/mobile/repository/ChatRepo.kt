package wse18.ase.qse03.mobile.repository

import androidx.lifecycle.MutableLiveData
import android.util.Log
import com.virgilsecurity.android.ethree.kotlin.interaction.EThree
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import wse18.ase.qse03.mobile.model.ChatMessage
import wse18.ase.qse03.mobile.model.ChatThread
import wse18.ase.qse03.mobile.service.ChatService
import wse18.ase.qse03.mobile.util.CryptoUtil
import java.io.File
import java.util.Arrays.asList
import javax.inject.Singleton

@Singleton
class ChatRepo {

    private var decryptedCount = 0
    private val chatService by lazy {
        ChatService.create()
    }

    fun getAllChats(chatList: MutableLiveData<List<ChatThread>>, idToken: String?) {

        val cb = chatService.findAllChatThreads(idToken)
        cb.enqueue(object : Callback<List<ChatThread>> {
            override fun onResponse(call: Call<List<ChatThread>>?, response: Response<List<ChatThread>>?) {
                if (response != null && response.isSuccessful) {
                    val chatThreads = response.body()
                    Log.i("ChatRepo", "Response body string: " + chatThreads.toString())
                    chatList.value = chatThreads
                } else {
                    Log.wtf("ChatRepo::getAllChats", "No data from callback findAllChatThreads " + response?.code())
                }
            }

            override fun onFailure(call: Call<List<ChatThread>>?, t: Throwable?) {
                Log.wtf("ChatRepo::getAllChats", "onFailure " + t.toString())
            }
        })
    }

    fun createChat(newChat: MutableLiveData<ChatThread>, idToken: String?, officeId: Long) {

        val cb = chatService.createChatThread(idToken, officeId)
        cb.enqueue(object : Callback<ChatThread> {
            override fun onResponse(call: Call<ChatThread>?, response: Response<ChatThread>?) {
                if (response != null && response.isSuccessful) {
                    val chatThread = response.body()
                    Log.i("ChatRepo", "Response body string: " + chatThread.toString())
                    newChat.value = chatThread
                } else {
                    Log.wtf("ChatRepo::createChat", "No data from callback createChatThread " + response?.code())
                }
            }

            override fun onFailure(call: Call<ChatThread>?, t: Throwable?) {
                Log.wtf("ChatRepo::createChat", "onFailure " + t.toString())
            }
        })
    }

    fun getAllChatMessages(messageList: MutableLiveData<List<ChatMessage>>, idToken: String?, eThree: EThree, receiverUIDs: List<String>, threadId: Long) {

        val cb = chatService.findChatMessages(idToken, threadId)
        cb.enqueue(object : Callback<List<ChatMessage>> {
            override fun onResponse(call: Call<List<ChatMessage>>?, response: Response<List<ChatMessage>>?) {
                if (response != null && response.isSuccessful) {
                    val chatMessages = response.body()
                    Log.i("ChatRepo", "Response body string: " + chatMessages.toString())
                    for (i in chatMessages!!.indices) {

                        decryptedCount = 0
                        val senderUid = if(chatMessages[i].patientMessage) receiverUIDs[0] else chatMessages[i].senderUid
                        CryptoUtil.decrypt(eThree, chatMessages[i].message, senderUid, { decrypted ->
                            chatMessages[i].message = decrypted
                            if (decryptedCount >= chatMessages.size - 1) {
                                messageList.postValue(chatMessages)
                            }
                            decryptedCount += 1
                        })
                    }
                } else {
                    Log.wtf(
                        "ChatRepo::getAllChatMessages",
                        "No data from callback findChatMessages " + response?.code()
                    )
                }
            }

            override fun onFailure(call: Call<List<ChatMessage>>?, t: Throwable?) {
                Log.wtf("ChatRepo::getAllChatMessages", "onFailure " + t.toString())
            }
        })
    }

    fun createChatMessage(newMessage: MutableLiveData<ChatMessage>, idToken: String?, eThree: EThree, receiverUIDs: List<String>, threadId: Long, message: String) {
        CryptoUtil.encrypt(eThree, message, receiverUIDs, { encrypted ->
            Log.d("ChatRepo::plainMsg", "Encrypted message: " + encrypted)
            val messageRequestBody = RequestBody.create(MediaType.parse("text/plain"), encrypted)
            val cb = chatService.createChatMessage(idToken, threadId, messageRequestBody)

            cb.enqueue(object : Callback<ChatMessage> {
                override fun onResponse(call: Call<ChatMessage>?, response: Response<ChatMessage>?) {
                    if (response != null && response.isSuccessful) {
                        val chatMessage = response.body()
                        Log.i("ChatRepo::plainMsg", "Response body string: " + chatMessage.toString())
                        // Not used
                        /*CryptoUtil.decrypt(eThree, chatMessage!!.message, receiverUIDs[0], { decrypted ->
                            chatMessage.message = decrypted
                            newMessage.postValue(chatMessage)
                        })*/
                    } else {
                        Log.wtf(
                                "ChatRepo::createChatMessage",
                                "No data from callback createChatMessage " + response?.code()
                        )
                    }
                }

                override fun onFailure(call: Call<ChatMessage>?, t: Throwable?) {
                    Log.wtf("ChatRepo::createChatMessage", "onFailure " + t.toString())
                }
            })
        })
    }

    fun createChatMessageWithAttachment(
        newMessage: MutableLiveData<ChatMessage>,
        idToken: String?,
        eThree: EThree,
        receiverUIDs: List<String>,
        threadId: Long,
        message: String,
        file: File
    ) {

        CryptoUtil.encrypt(eThree, message, receiverUIDs, { encrypted ->
            Log.d("ChatRepo::msgAttach", "Encrypted message: " + encrypted)
            val messageRequestBody = RequestBody.create(MediaType.parse("text/plain"), encrypted)

            // creates RequestBody instance from file
            var bodyFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            // MultipartBody.Part is used to send also the actual filename
            var partBodyFile = MultipartBody.Part.createFormData("file", file.name, bodyFile)


            val cb = chatService.createMessageWithAttachment(idToken, threadId, messageRequestBody, partBodyFile)

            cb.enqueue(object : Callback<ChatMessage> {
                override fun onResponse(call: Call<ChatMessage>?, response: Response<ChatMessage>?) {
                    if (response != null && response.isSuccessful) {
                        val chatMessage = response.body()
                        Log.i("ChatRepo::msgAttach", "Response body string: " + chatMessage.toString())
                        // Not used
                        /*CryptoUtil.decrypt(eThree, chatMessage!!.message, receiverUIDs[0], { decrypted ->
                            chatMessage.message = decrypted
                            newMessage.postValue(chatMessage)
                        })*/
                    } else {
                        Log.wtf(
                                "ChatRepo::createChatMessage",
                                "No data from callback createChatMessage " + response?.code()
                        )
                    }
                }

                override fun onFailure(call: Call<ChatMessage>?, t: Throwable?) {
                    Log.wtf("ChatRepo::createChatMessage", "onFailure " + t.toString())
                }
            })
        })
    }

    fun getChatByOfficeId(chat: MutableLiveData<ChatThread>, idToken: String?, officeId: Long) {

        val cb = chatService.findChatThreadByOfficeId(idToken, officeId)
        cb.enqueue(object : Callback<ChatThread> {
            override fun onResponse(call: Call<ChatThread>?, response: Response<ChatThread>?) {
                if (response != null && response.isSuccessful) {
                    val chatThread = response.body()
                    Log.i("ChatRepo", "Response body string: " + chatThread.toString())
                    chat.value = chatThread
                } else {
                    Log.wtf(
                        "ChatRepo::getChatThreadByOfficeId",
                        "No data from callback findChatThreadByOfficeId " + response?.code()
                    )
                }
            }

            override fun onFailure(call: Call<ChatThread>?, t: Throwable?) {
                Log.wtf("ChatRepo::getChatThreadByOfficeId", "onFailure " + t.toString())
            }
        })
    }

}