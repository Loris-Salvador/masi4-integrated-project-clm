package be.hepl.clm.data.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitAuthApi {
    @POST("customer/login/email")
    suspend fun login(@Body request: LoginRequest): Response<String>

    @POST("customer/login/email/challenge")
    suspend fun emailChallenge(@Body request: ChallengeRequest): Response<ChallengeResponse>

    @POST("customer/login/phone/challenge")
    suspend fun phoneChallenge(@Body request: ChallengeRequest): Response<ChallengeResponse>
}