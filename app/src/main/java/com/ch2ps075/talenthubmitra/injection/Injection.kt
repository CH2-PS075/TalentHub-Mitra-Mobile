package com.ch2ps075.talenthubmitra.injection

import android.content.Context
import com.ch2ps075.talenthubmitra.data.network.api.retrofit.ApiConfig
import com.ch2ps075.talenthubmitra.data.preference.TalentPreferences
import com.ch2ps075.talenthubmitra.data.preference.dataStore
import com.ch2ps075.talenthubmitra.data.repo.AuthRepository

object Injection {
    fun provideAuthRepository(context: Context): AuthRepository {
        val pref = TalentPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return AuthRepository.getInstance(apiService, pref)
    }
}