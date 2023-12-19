package com.ch2ps075.talenthubmitra.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.ch2ps075.talenthubmitra.data.network.api.response.AuthResponse
import com.ch2ps075.talenthubmitra.data.network.api.retrofit.ApiService
import com.ch2ps075.talenthubmitra.data.preference.TalentModel
import com.ch2ps075.talenthubmitra.data.preference.TalentPreferences
import com.ch2ps075.talenthubmitra.state.ResultState
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class AuthRepository private constructor(
    private val apiService: ApiService,
    private val talentPreferences: TalentPreferences,
) {

    fun getSession(): Flow<TalentModel> {
        return talentPreferences.getSession()
    }

    suspend fun saveSession(user: TalentModel) {
        talentPreferences.saveSession(user)
    }

    suspend fun logout() {
        talentPreferences.logout()
    }

    fun register(
        username: String,
        email: String,
        password: String,
    ): LiveData<ResultState<Any>> {
        return liveData {
            emit(ResultState.Loading)
            try {
                val successResponse = apiService.register(username, email, password).message
                emit(ResultState.Success(successResponse))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, AuthResponse::class.java)
                errorBody.message.let { ResultState.Error(it) }.let { emit(it) }
            }
        }
    }

    fun login(
        email: String,
        password: String,
    ) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.login(email, password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, AuthResponse::class.java)
            errorBody.message.let { ResultState.Error(it) }.let { emit(it) }
        }
    }


    companion object {
        @Volatile
        private var instance: AuthRepository? = null
        fun getInstance(
            apiService: ApiService,
            talentPreferences: TalentPreferences,
        ): AuthRepository = instance ?: synchronized(this) {
            instance ?: AuthRepository(apiService, talentPreferences)
        }.also { instance = it }
    }
}