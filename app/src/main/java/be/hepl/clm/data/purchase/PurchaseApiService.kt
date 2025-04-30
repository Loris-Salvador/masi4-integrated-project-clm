package be.hepl.clm.data.purchase

import be.hepl.clm.domain.PurchaseRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface PurchaseApiService {
    @POST("purchase/cart/validate")
    suspend fun validateCart(@Body purchaseRequest: PurchaseRequest): Any
}