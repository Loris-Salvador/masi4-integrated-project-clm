package be.hepl.clm.data.bank

import be.hepl.clm.data.token.TokenRepository
import be.hepl.clm.domain.BankTokenRequest
import be.hepl.clm.domain.PaymentInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BankService @Inject constructor(
    private val bankWebSocketService: BankWebSocketService,
    private val bankApiService: BankApiService,
    private val tokenRepository: TokenRepository
) {
    suspend fun processPayment(paymentInfo: PaymentInfo): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            // Étape 1: Obtenir le token bancaire via WebSocket
            val tokenResult = bankWebSocketService.getBankToken(paymentInfo.cardNumber)

            if (tokenResult.isFailure) {
                return@withContext Result.failure(tokenResult.exceptionOrNull() ?: Exception("Erreur lors de l'obtention du token bancaire"))
            }

            val bankToken = tokenResult.getOrThrow()

            // Étape 2: Envoyer le token bancaire au serveur
            val userToken = tokenRepository.getToken() ?: return@withContext Result.failure(Exception("Aucun token utilisateur disponible"))

            val bankTokenRequest = BankTokenRequest(
                bankToken = bankToken,
                customerBank = paymentInfo.customerBank,
                numCardCustomer = paymentInfo.cardNumber,
                communication = paymentInfo.communication,
                userToken = userToken
            )

            val response = bankApiService.sendBankToken(bankTokenRequest)

            if (!response.isSuccessful) {
                return@withContext Result.failure(Exception("Erreur lors de l'envoi du token bancaire: ${response.code()} ${response.message()}"))
            }

            val paymentResponse = response.body() ?: return@withContext Result.failure(Exception("Réponse de paiement vide"))

            // Vérifier la réponse du serveur
            return@withContext if (paymentResponse.serverResponse == "ACK") {
                Result.success(true)
            } else {
                Result.failure(Exception("Le paiement a été refusé par le serveur: ${paymentResponse.serverResponse}"))
            }

        } catch (e: Exception) {
            return@withContext Result.failure(e)
        }
    }
}