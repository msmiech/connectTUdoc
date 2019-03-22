package wse18.ase.qse03.mobile.service

import retrofit2.Call
import retrofit2.http.*
import wse18.ase.qse03.mobile.model.ChatMessage
import wse18.ase.qse03.mobile.model.ChatThread
import wse18.ase.qse03.mobile.util.RetrofitUtil
import okhttp3.RequestBody
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.POST
import retrofit2.http.Multipart


interface ChatService {

    @GET("/chat/thread")
    fun findAllChatThreads(@Header("X-Firebase-Auth") idToken: String?): Call<List<ChatThread>>

    @POST("/chat/thread")
    fun createChatThread(@Header("X-Firebase-Auth") idToken: String?, @Body id: Long): Call<ChatThread>

    @GET("/chat/message/{threadID}")
    fun findChatMessages(@Header("X-Firebase-Auth") idToken: String?, @Path("threadID") threadId: Long): Call<List<ChatMessage>>

    @POST("/chat/message/{threadID}")
    fun createChatMessage(@Header("X-Firebase-Auth") idToken: String?, @Path("threadID") threadId: Long, @Body message: RequestBody): Call<ChatMessage>

    @Multipart
    @POST("/chat/attachment/{threadID}")
    fun createMessageWithAttachment(
        @Header("X-Firebase-Auth") idToken: String?,
        @Path("threadID") threadId: Long,
        @Part("message") message: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<ChatMessage>

    @GET("/chat/officeThread/{officeID}")
    fun findChatThreadByOfficeId(@Header("X-Firebase-Auth") idToken: String?, @Path("officeID") officeId: Long): Call<ChatThread>

    companion object {
        fun create(): ChatService {

            return RetrofitUtil.getMedconnectRetrofit().create(ChatService::class.java)
        }
    }
}