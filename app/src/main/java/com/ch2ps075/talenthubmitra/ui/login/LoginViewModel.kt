package com.ch2ps075.talenthubmitra.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ch2ps075.talenthubmitra.data.preference.TalentModel
import com.ch2ps075.talenthubmitra.data.repo.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun login(email: String, password: String) = authRepository.login(email, password)

    fun saveSession(user: TalentModel) {
        viewModelScope.launch {
            authRepository.saveSession(user)
        }
    }
}