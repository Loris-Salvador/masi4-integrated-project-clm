package be.hepl.clm.data.auth

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>

    suspend fun emailChallenge(email: String, challenge: String): Result<ChallengeResponse>

    suspend fun phoneChallenge(email: String, challenge: String): Result<ChallengeResponse>
}
