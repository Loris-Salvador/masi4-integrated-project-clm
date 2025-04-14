package be.hepl.clm.data.token

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.dataStore by preferencesDataStore(name = "auth_prefs")

object PreferencesKeys {
    val TOKEN = stringPreferencesKey("token")
}

suspend fun saveToken(context: Context, token: String) {
    context.dataStore.edit { prefs ->
        prefs[PreferencesKeys.TOKEN] = token
    }
}

suspend fun getToken(context: Context): String? {
    val prefs = context.dataStore.data.first()
    return prefs[PreferencesKeys.TOKEN]
}