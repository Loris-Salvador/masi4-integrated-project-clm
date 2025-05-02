package be.hepl.clm.domain

import java.time.LocalDateTime

// Modèle pour un message de chat
data class ChatMessage(
    val id: Long? = null,
    val chatId: Long,
    val username: String,
    val message: String,
    val timestamp: LocalDateTime? = null,
    val isCurrentUser: Boolean = false  // Pour différencier visuellement les messages de l'utilisateur
)

// Modèle pour l'envoi d'un message
data class SendMessageRequest(
    val chatId: Long,
    val username: String,
    val message: String
)