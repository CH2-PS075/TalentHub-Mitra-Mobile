package com.ch2ps075.talenthubmitra.data.network.api.retrofit

import com.ch2ps075.talenthubmitra.data.network.api.response.AuthResponse
import com.ch2ps075.talenthubmitra.data.network.api.response.PredictionResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): AuthResponse

    @FormUrlEncoded
    @POST("auth/talents/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): AuthResponse

    @Multipart
    @POST("prediction")
    suspend fun prediction(
        @Part file: MultipartBody.Part
    ): PredictionResponse
}