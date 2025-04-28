package be.hepl.clm.data.auth

import javax.inject.Inject
import javax.inject.Named

class AuthRepositoryImpl @Inject constructor(private val api: RetrofitAuthApi) : AuthRepository {

    override suspend fun emailLogin(email: String, password: String): Result<String> {
        return try {
            val response = api.emailLogin(LoginRequest(email, password))

            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()!!.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun phoneLogin(email: String, password: String): Result<String> {
        return try {
            val response = api.phoneLogin(LoginRequest(email, password))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()!!.string()))
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
                Result.failure(Exception(response.errorBody()!!.string()))
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
                Result.failure(Exception(response.errorBody()!!.string()))
            }
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signup() {
        TODO("Not yet implemented")
    }
}