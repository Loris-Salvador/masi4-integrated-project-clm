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
}
