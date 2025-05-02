package be.hepl.clm.data.chat

import be.hepl.clm.domain.ChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

interface ChatRepository {
    val messages: Flow<List<ChatMessage>>
    suspend fun refreshMessages(chatId: Long, username: String)
    suspend fun sendMessage(chatId: Long, username: String, message: String): Result<Unit>
}

@Singleton
class ChatRepositoryImpl @Inject constructor(
    private val chatService: ChatService
) : ChatRepository {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    override val messages = _messages.asStateFlow()

    override suspend fun refreshMessages(chatId: Long, username: String) {
        chatService.getConversation(chatId, username).fold(
            onSuccess = { newMessages ->
                _messages.update { newMessages }
            },
            onFailure = { error ->
                // Log error but don't update messages
                // We could add an error state here if needed
            }
        )
    }

    override suspend fun sendMessage(chatId: Long, username: String, message: String): Result<Unit> {
        return chatService.sendMessage(chatId, username, message)
    }
}