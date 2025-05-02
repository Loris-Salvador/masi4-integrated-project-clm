package be.hepl.clm.data.chat

import be.hepl.clm.domain.ChatMessage
import be.hepl.clm.domain.SendMessageRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApiService {
    @GET("webchat/conversation/{chatId}")
    suspend fun getConversation(@Path("chatId") chatId: Long): Response<List<ChatMessage>>

    @POST("webchat")
    suspend fun sendMessage(@Body request: SendMessageRequest): Response<Unit>
}