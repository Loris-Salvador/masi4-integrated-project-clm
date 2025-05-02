package be.hepl.clm.data.bank

import be.hepl.clm.domain.BankTokenResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URI
import javax.inject.Inject
import javax.inject.Singleton
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake

@Singleton
class BankWebSocketService @Inject constructor() {

    private val SERVER_URL = "ws://95.182.245.78:59008"

    suspend fun getBankToken(cardNumber: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            var token: String? = null
            var connectionError: Exception? = null

            val client = object : WebSocketClient(URI(SERVER_URL)) {
                override fun onOpen(handshakedata: ServerHandshake?) {
                    send(cardNumber)
                }

                override fun onMessage(message: String?) {
                    token = message
                    close()
                }

                override fun onClose(code: Int, reason: String?, remote: Boolean) {
                }

                override fun onError(ex: Exception?) {
                    connectionError = ex
                    close()
                }
            }

            client.connectBlocking()

            // Wait for the connection to complete and message to be received
            while (client.isOpen && token == null && connectionError == null) {
                Thread.sleep(100)
            }

            // Check if there was an error
            if (connectionError != null) {
                throw connectionError!!
            }

            if (token == null) {
                return@withContext Result.failure(Exception("Aucun token reçu du serveur bancaire"))
            }

            return@withContext Result.success(token!!)
        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }
}