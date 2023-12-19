package com.ch2ps075.talenthubmitra.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ch2ps075.talenthubmitra.data.preference.TalentModel
import com.ch2ps075.talenthubmitra.data.repo.AuthRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    fun getSession(): LiveData<TalentModel> {
        return authRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

}