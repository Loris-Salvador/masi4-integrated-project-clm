package be.hepl.clm.data.auth

import be.hepl.clm.domain.CustomerSignup
import be.hepl.clm.domain.VerifyRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitAuthApi {
    @POST("customer/login/email")
    suspend fun emailLogin(@Body request: LoginRequest): Response<String>

    @POST("customer/login/phone")
    suspend fun phoneLogin(@Body request: LoginRequest): Response<String>

    @POST("customer/login/email/challenge")
    suspend fun emailChallenge(@Body request: ChallengeRequest): Response<ChallengeResponse>

    @POST("customer/login/phone/challenge")
    suspend fun phoneChallenge(@Body request: ChallengeRequest): Response<ChallengeResponse>

    @POST("customer/signup")
    suspend fun signup(@Body signupRequest: CustomerSignup): Response<String>

    @POST("customer/verify/email")
    suspend fun verifyEmail(@Body verifyRequest: VerifyRequest): Response<String>

    @POST("customer/verify/email/challenge")
    suspend fun verifyEmailChallenge(@Body request: ChallengeRequest): Response<String>
}