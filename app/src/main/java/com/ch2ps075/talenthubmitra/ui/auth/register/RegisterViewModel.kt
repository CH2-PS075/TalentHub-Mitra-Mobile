package com.ch2ps075.talenthubmitra.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ch2ps075.talenthubmitra.data.repo.AuthRepository
import com.ch2ps075.talenthubmitra.state.ResultState
import java.io.File

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {
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
        imageFile: File,
    ): LiveData<ResultState<Any>> {
        return authRepository.register(
            talentName,
            category,
            quantity,
            address,
            contact,
            price,
            email,
            password,
            portfolio,
            imageFile
        )
    }
}