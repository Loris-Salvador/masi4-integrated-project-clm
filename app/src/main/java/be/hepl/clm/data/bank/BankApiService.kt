package be.hepl.clm.data.bank
import be.hepl.clm.domain.BankTokenRequest
import be.hepl.clm.domain.PaymentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BankApiService {
    @POST("checkout/send-token")
    suspend fun sendBankToken(@Body request: BankTokenRequest): Response<PaymentResponse>
}