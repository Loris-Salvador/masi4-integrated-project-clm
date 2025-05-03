package be.hepl.clm.data.purchase

import be.hepl.clm.data.bank.BankService
import be.hepl.clm.data.token.TokenRepository
import be.hepl.clm.domain.PurchaseRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PurchaseRepositoryImpl @Inject constructor(
    private val purchaseService: PurchaseService,
    private val bankService: BankService,
    private val tokenRepository: TokenRepository
) : PurchaseRepository {

    override suspend fun validateCart(purchaseRequest: PurchaseRequest): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                purchaseRequest.token = tokenRepository.getToken() ?: ""


                try {
                    purchaseService.validateCart(purchaseRequest)
                    true
                } catch (e: Exception) {
                    false
                }

                val paymentResult = bankService.processPayment(purchaseRequest.paymentInfo)

                if (paymentResult.isFailure) {
                    return@withContext false
                }

                true



            } catch (e: Exception) {
                false
            }
        }
    }
}