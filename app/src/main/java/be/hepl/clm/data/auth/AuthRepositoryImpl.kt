package be.hepl.clm.data.auth

import be.hepl.clm.domain.CustomerSignup
import be.hepl.clm.domain.VerifyRequest
import javax.inject.Inject

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
        } catch (e: Exception) {
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
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signup(customerSignup: CustomerSignup): Result<String> {
        return try {

            val response = api.signup(customerSignup)

            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override suspend fun verifyEmail(verifyRequest: VerifyRequest): Result<String> {
        return try {
            val response = api.verifyEmail(verifyRequest)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()!!.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyEmailChallenge(email: String, challenge: String): Result<String> {
        return try {
            val response = api.verifyEmailChallenge(ChallengeRequest(email, challenge))
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()!!.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}