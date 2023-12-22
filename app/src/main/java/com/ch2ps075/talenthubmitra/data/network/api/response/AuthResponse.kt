package com.ch2ps075.talenthubmitra.data.network.api.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("contact")
    val contact: String,

    @field:SerializedName("address")
    val address: String,

    @field:SerializedName("picture")
    val picture: String,

    @field:SerializedName("token")
    val token: String,
)