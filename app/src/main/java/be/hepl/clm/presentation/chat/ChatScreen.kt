package be.hepl.clm.presentation.chat


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import be.hepl.clm.domain.ChatMessage
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    // Affichage des erreurs
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
        }
    }

    // Scroll automatique vers le dernier message
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Service Client") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Zone des messages
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (uiState.messages.isEmpty() && !uiState.isLoading) {
                    // Aucun message
                    Text(
                        text = "Aucun message. Commencez la conversation!",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    // Liste des messages
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        items(uiState.messages) { message ->
                            MessageItem(message = message)
                        }

                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }

                // Indicateur de chargement
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            // Zone de saisie du message
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = uiState.currentMessage,
                    onValueChange = { viewModel.updateCurrentMessage(it) },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Tapez votre message...") },
                    maxLines = 3
                )

                IconButton(
                    onClick = {
                        viewModel.sendMessage()
                        focusManager.clearFocus()
                    },
                    enabled = uiState.currentMessage.isNotBlank() && !uiState.isLoading
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Envoyer",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MessageItem(message: ChatMessage) {
    val alignment = if (message.isCurrentUser) Alignment.End else Alignment.Start
    val backgroundColor = if (message.isCurrentUser)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.surfaceVariant

    val textColor = if (message.isCurrentUser)
        MaterialTheme.colorScheme.onPrimary
    else
        MaterialTheme.colorScheme.onSurfaceVariant

    val formatter = DateTimeFormatter.ofPattern("HH:mm")

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = alignment
    ) {
        // Nom d'utilisateur
        Text(
            text = message.username,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
        )

        // Bulle de message
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = if (message.isCurrentUser) 16.dp else 4.dp,
                        topEnd = if (message.isCurrentUser) 4.dp else 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
                .background(backgroundColor)
                .padding(12.dp)
        ) {
            Column {
                // Contenu du message
                Text(
                    text = message.message,
                    color = textColor
                )

                // Horodatage
                message.timestamp?.let {
                    Text(
                        text = it.format(formatter),
                        style = MaterialTheme.typography.bodySmall,
                        color = textColor.copy(alpha = 0.7f),
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}