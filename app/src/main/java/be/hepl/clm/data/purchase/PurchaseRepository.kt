package be.hepl.clm.data.purchase

import be.hepl.clm.domain.BillingInfo
import be.hepl.clm.domain.PurchaseRequest

interface PurchaseRepository {
    suspend fun validateCart(purchaseRequest: PurchaseRequest): Boolean
}