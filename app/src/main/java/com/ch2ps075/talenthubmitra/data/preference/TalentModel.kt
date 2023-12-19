package com.ch2ps075.talenthubmitra.data.preference

data class TalentModel(
    val username: String,
    val email: String,
    val token: String,
    val isLogin: Boolean = false,
)
