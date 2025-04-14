package be.hepl.clm.data.auth

import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val api: RetrofitAuthApi) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<String> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erreur HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun emailChallenge(
        email: String,
        challenge: String
    ): Result<ChallengeResponse> {

        return try {
            val response = api.emailChallenge(ChallengeRequest(email, challenge))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erreur HTTP ${response.code()}"))
            }
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun phoneChallenge(
        email: String,
        challenge: String
    ): Result<ChallengeResponse> {
        return try {
            val response = api.phoneChallenge(ChallengeRequest(email, challenge))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Erreur HTTP ${response.code()}"))
            }
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }
}