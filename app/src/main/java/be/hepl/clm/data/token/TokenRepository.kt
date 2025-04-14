package be.hepl.clm.data.token

interface TokenRepository {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
}