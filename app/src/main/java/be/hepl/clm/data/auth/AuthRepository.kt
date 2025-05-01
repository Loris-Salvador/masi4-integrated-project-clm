package be.hepl.clm.data.auth

import be.hepl.clm.domain.CustomerSignupDTO

interface AuthRepository {
    suspend fun emailLogin(email: String, password: String): Result<String>

    suspend fun phoneLogin(email: String, password: String): Result<String>

    suspend fun emailChallenge(email: String, challenge: String): Result<ChallengeResponse>

    suspend fun phoneChallenge(email: String, challenge: String): Result<ChallengeResponse>

    suspend fun signup(customerSignupDTO: CustomerSignupDTO): Result<String>
}
