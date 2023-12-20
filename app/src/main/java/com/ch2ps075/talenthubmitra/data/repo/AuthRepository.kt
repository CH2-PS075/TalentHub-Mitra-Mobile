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
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

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
        talentName: String,
        category: String,
        quantity: String,
        address: String,
        contact: String,
        price: String,
        email: String,
        password: String,
        portfolio: String,
        imageFile: File
    ): LiveData<ResultState<Any>> {
        return liveData {
            emit(ResultState.Loading)
            val talentNameRequestBody = talentName.toRequestBody("text/plain".toMediaType())
            val categoryRequestBody = category.toRequestBody("text/plain".toMediaType())
            val quantityRequestBody = quantity.toRequestBody("text/plain".toMediaType())
            val addressRequestBody = address.toRequestBody("text/plain".toMediaType())
            val contactRequestBody = contact.toRequestBody("text/plain".toMediaType())
            val priceRequestBody = price.toRequestBody("text/plain".toMediaType())
            val emailRequestBody = email.toRequestBody("text/plain".toMediaType())
            val passwordRequestBody = password.toRequestBody("text/plain".toMediaType())
            val portfolioRequestBody = portfolio.toRequestBody("text/plain".toMediaType())

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "picture",
                imageFile.name,
                requestImageFile
            )
            try {
                val successResponse = apiService.register(
                    talentNameRequestBody,
                    categoryRequestBody,
                    quantityRequestBody,
                    addressRequestBody,
                    contactRequestBody,
                    priceRequestBody,
                    emailRequestBody,
                    passwordRequestBody,
                    portfolioRequestBody,
                    multipartBody
                )
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