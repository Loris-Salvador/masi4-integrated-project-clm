package be.hepl.clm.data.token

import android.content.Context

class TokenDataStoreRepository(
    private val context: Context
) : TokenRepository {

    override suspend fun saveToken(token: String) {
        saveToken(context, token)
    }

    override suspend fun getToken(): String? {
        return getToken(context)
    }
}