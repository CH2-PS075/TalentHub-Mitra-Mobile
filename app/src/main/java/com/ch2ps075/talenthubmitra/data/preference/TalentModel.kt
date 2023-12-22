package com.ch2ps075.talenthubmitra.data.preference

data class TalentModel(
    val email: String,
    val contact: String,
    val address: String,
    val picture: String,
    val token: String,
    val isLogin: Boolean = false,
)
