package be.hepl.clm.data.auth

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String>
}
