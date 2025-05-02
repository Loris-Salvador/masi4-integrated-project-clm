package be.hepl.clm.data.chat

import be.hepl.clm.domain.ChatMessage
import be.hepl.clm.domain.SendMessageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatService @Inject constructor(
    private val chatApiService: ChatApiService
) {
    suspend fun getConversation(chatId: Long, currentUsername: String): Result<List<ChatMessage>> = withContext(Dispatchers.IO) {
        try {
            val response = chatApiService.getConversation(chatId)

            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("Erreur lors de la récupération de la conversation: ${response.code()} ${response.message()}"))
            }

            val messages = response.body() ?: emptyList()

            // Marquer les messages de l'utilisateur courant
            val messagesWithUserFlag = messages.map { message ->
                message.copy(isCurrentUser = message.username == currentUsername)
            }

            return@withContext Result.success(messagesWithUserFlag)
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }

    suspend fun sendMessage(chatId: Long, username: String, messageText: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val request = SendMessageRequest(
                chatId = chatId,
                username = username,
                message = messageText
            )

            val response = chatApiService.sendMessage(request)

            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("Erreur lors de l'envoi du message: ${response.code()} ${response.message()}"))
            }

            return@withContext Result.success(Unit)
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }
}