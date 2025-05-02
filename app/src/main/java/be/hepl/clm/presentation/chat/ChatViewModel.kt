package be.hepl.clm.presentation.chat


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.hepl.clm.data.chat.ChatRepository
import be.hepl.clm.domain.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentMessage: String = "",
    val username: String = "Client" // Nom d'utilisateur par défaut
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository
) : ViewModel() {

    // Constante pour le chatId
    private val chatId: Long = 3

    // UI State
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {
        // Initialiser la récupération des messages
        refreshMessages()

        // Observer les messages du repository
        viewModelScope.launch {
            chatRepository.messages.collect { messages ->
                _uiState.update { it.copy(messages = messages) }
            }
        }

        // Rafraîchir périodiquement les messages (polling)
        startMessagePolling()
    }

    fun setUsername(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun updateCurrentMessage(message: String) {
        _uiState.update { it.copy(currentMessage = message) }
    }

    fun sendMessage() {
        val message = _uiState.value.currentMessage.trim()
        val username = _uiState.value.username

        if (message.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            chatRepository.sendMessage(chatId, username, message).fold(
                onSuccess = {
                    _uiState.update { it.copy(
                        isLoading = false,
                        currentMessage = "",
                        error = null
                    )}

                    // Actualiser les messages après l'envoi
                    refreshMessages()
                },
                onFailure = { error ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = "Erreur lors de l'envoi du message: ${error.message}"
                    )}
                }
            )
        }
    }

    private fun refreshMessages() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                chatRepository.refreshMessages(chatId, _uiState.value.username)
                _uiState.update { it.copy(isLoading = false, error = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = "Erreur lors de la récupération des messages: ${e.message}"
                )}
            }
        }
    }

    private fun startMessagePolling() {
        viewModelScope.launch {
            while (true) {
                delay(5000) // Actualiser toutes les 5 secondes
                refreshMessages()
            }
        }
    }
}