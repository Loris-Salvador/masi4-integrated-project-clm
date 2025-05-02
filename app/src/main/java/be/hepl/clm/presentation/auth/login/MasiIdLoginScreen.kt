package be.hepl.clm.presentation.auth.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import be.hepl.clm.presentation.navigation.Destinations
import kotlinx.coroutines.launch

@Composable
fun MasiIdLoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MasiIdViewModel = hiltViewModel()
) {
    val phoneNumber = viewModel.phoneNumber.collectAsState()
    val connectionState = viewModel.connectionState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(connectionState.value) {
        when (connectionState.value) {
            is MasiIdConnectionState.Error -> {
                Toast.makeText(
                    context,
                    (connectionState.value as MasiIdConnectionState.Error).message,
                    Toast.LENGTH_LONG
                ).show()
            }
            is MasiIdConnectionState.Success -> {
                Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                navController.navigate(Destinations.HOME) {
                    popUpTo(Destinations.LOGIN) { inclusive = true }
                }
            }
            else -> {}
        }
    }

    // Handle WebSocket connection
    LaunchedEffect(Unit) {
        viewModel.connectWebSocket()
    }

    // Clean up WebSocket on disposal
    DisposableEffect(Unit) {
        onDispose {
            viewModel.disconnectWebSocket()
        }
    }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "MASI ID Login",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            OutlinedTextField(
                value = phoneNumber.value,
                onValueChange = { viewModel.updatePhoneNumber(it) },
                label = { Text("Phone Number") },
                placeholder = { Text("Enter your phone number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true
            )

            Button(
                onClick = {
                    scope.launch {
                        viewModel.sendMasiIdLoginRequest()
                    }
                },
                enabled = phoneNumber.value.isNotBlank() &&
                        connectionState.value !is MasiIdConnectionState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                if (connectionState.value is MasiIdConnectionState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Login with MASI ID")
                }
            }

            when (connectionState.value) {
                is MasiIdConnectionState.WaitingForAppConfirmation -> {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        "Please check your MASI ID app to confirm login",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator()
                }
                else -> {}
            }
        }
    }
}