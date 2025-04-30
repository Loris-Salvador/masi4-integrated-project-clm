package be.hepl.clm.data.purchase

import be.hepl.clm.data.token.TokenRepository
import be.hepl.clm.domain.PurchaseRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PurchaseService @Inject constructor(
    private val purchaseApiService: PurchaseApiService,
    private val tokenRepository: TokenRepository
) {
    suspend fun validateCart(purchaseRequest: PurchaseRequest): Any {
        return withContext(Dispatchers.IO) {
            try {
                // Make sure we're using the latest token
                val token = tokenRepository.getToken() ?: ""
                val requestWithToken = purchaseRequest.copy(token = token)
                purchaseApiService.validateCart(requestWithToken)
            } catch (e: Exception) {
                e.printStackTrace()
                throw e
            }
        }
    }
}
