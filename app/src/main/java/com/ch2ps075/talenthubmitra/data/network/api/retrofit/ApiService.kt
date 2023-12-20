package com.ch2ps075.talenthubmitra.data.network.api.retrofit

import com.ch2ps075.talenthubmitra.data.network.api.response.AuthResponse
import com.ch2ps075.talenthubmitra.data.network.api.response.PredictionResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @Multipart
    @POST("talents")
    suspend fun register(
        @Part("talentName") talentName: RequestBody,
        @Part("category") category: RequestBody,
        @Part("quantity") quantity: RequestBody,
        @Part("address") address: RequestBody,
        @Part("contact") contact: RequestBody,
        @Part("price") price: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("portfolio") portfolio: RequestBody,
        @Part file: MultipartBody.Part,
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