package be.hepl.clm.presentation.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.hepl.clm.data.token.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URI
import javax.inject.Inject
import okhttp3.*
import okio.ByteString

sealed class MasiIdConnectionState {
    object Initial : MasiIdConnectionState()
    object Connecting : MasiIdConnectionState()
    object Connected : MasiIdConnectionState()
    object Loading : MasiIdConnectionState()
    object WaitingForAppConfirmation : MasiIdConnectionState()
    data class Success(val token: String) : MasiIdConnectionState()
    data class Error(val message: String) : MasiIdConnectionState()
}

@HiltViewModel
class MasiIdViewModel @Inject constructor(
    private val tokenRepository: TokenRepository
) : ViewModel() {

    private val TAG = "MasiIdViewModel"
    private val WEBSOCKET_URL = "ws://95.182.245.78:59001/ws/customer/login/masi-id"

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> = _phoneNumber

    private val _connectionState = MutableStateFlow<MasiIdConnectionState>(MasiIdConnectionState.Initial)
    val connectionState: StateFlow<MasiIdConnectionState> = _connectionState

    private var webSocket: WebSocket? = null
    private val client = OkHttpClient()

    fun updatePhoneNumber(number: String) {
        _phoneNumber.value = number
    }

    fun connectWebSocket() {
        if (webSocket != null) return

        _connectionState.value = MasiIdConnectionState.Connecting

        val request = Request.Builder()
            .url(WEBSOCKET_URL)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d(TAG, "WebSocket connection established")
                _connectionState.value = MasiIdConnectionState.Connected
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.d(TAG, "Message received: $text")
                processWebSocketResponse(text)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
                Log.d(TAG, "ByteString message received")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                Log.d(TAG, "WebSocket closing: $code, $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                Log.d(TAG, "WebSocket closed: $code, $reason")
                this@MasiIdViewModel.webSocket = null
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.e(TAG, "WebSocket failure", t)
                _connectionState.value = MasiIdConnectionState.Error("Connection failed: ${t.message}")
                this@MasiIdViewModel.webSocket = null
            }
        })
    }

    fun disconnectWebSocket() {
        webSocket?.close(1000, "User navigated away")
        webSocket = null
        _connectionState.value = MasiIdConnectionState.Initial
    }

    fun sendMasiIdLoginRequest() {
        if (webSocket == null) {
            connectWebSocket()
            return
        }

        if (_phoneNumber.value.isBlank()) {
            _connectionState.value = MasiIdConnectionState.Error("Phone number cannot be empty")
            return
        }

        _connectionState.value = MasiIdConnectionState.Loading

        val jsonRequest = JSONObject().apply {
            put("phoneNumber", _phoneNumber.value)
        }

        webSocket?.send(jsonRequest.toString())
    }

    private fun processWebSocketResponse(response: String) {
        try {
            val jsonObject = JSONObject(response)
            val status = jsonObject.getString("status")

            when (status) {
                "SEND" -> {
                    // First confirmation, waiting for app validation
                    _connectionState.value = MasiIdConnectionState.WaitingForAppConfirmation
                }
                "OK" -> {
                    // Success, user confirmed in the app
                    val tokenObj = jsonObject.getJSONObject("token")
                    val accessToken = tokenObj.getString("accessToken")

                    // Save token to repository
                    viewModelScope.launch {
                        tokenRepository.saveToken(accessToken)
                        _connectionState.value = MasiIdConnectionState.Success(accessToken)
                    }
                }
                else -> {
                    _connectionState.value = MasiIdConnectionState.Error("Unknown status: $status")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error processing response", e)
            _connectionState.value = MasiIdConnectionState.Error("Error processing response: ${e.message}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnectWebSocket()
    }
}