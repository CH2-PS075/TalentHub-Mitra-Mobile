package com.ch2ps075.talenthubmitra.data.network.api.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("token")
    val token: String,
)