package be.hepl.clm.data.purchase

import be.hepl.clm.data.token.TokenRepository
import be.hepl.clm.domain.BillingInfo
import be.hepl.clm.domain.PurchaseRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PurchaseRepositoryImpl @Inject constructor(
    private val purchaseService: PurchaseService,
    private val tokenRepository: TokenRepository
) : PurchaseRepository {

    override suspend fun validateCart(purchaseRequest: PurchaseRequest): Boolean {
        return withContext(Dispatchers.IO) {
            purchaseRequest.token = tokenRepository.getToken().toString();
            try {
                purchaseService.validateCart(purchaseRequest)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}