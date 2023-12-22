package com.ch2ps075.talenthubmitra.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class TalentPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getSession(): Flow<TalentModel> {
        return dataStore.data.map { preferences ->
            TalentModel(
                email = preferences[EMAIL_KEY] ?: "",
                contact = preferences[CONTACT_KEY] ?: "",
                address = preferences[ADDRESS_KEY] ?: "",
                picture = preferences[PICTURE_KEY] ?: "",
                token = preferences[TOKEN_KEY] ?: "",
                isLogin = preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }

    suspend fun saveSession(user: TalentModel) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = user.email
            preferences[CONTACT_KEY] = user.contact
            preferences[ADDRESS_KEY] = user.address
            preferences[PICTURE_KEY] = user.picture
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val CONTACT_KEY = stringPreferencesKey("contact")
        private val ADDRESS_KEY = stringPreferencesKey("address")
        private val PICTURE_KEY = stringPreferencesKey("picture")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        @Volatile
        private var INSTANCE: TalentPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): TalentPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = TalentPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}