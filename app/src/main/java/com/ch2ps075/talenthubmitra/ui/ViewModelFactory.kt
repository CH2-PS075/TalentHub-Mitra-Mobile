package com.ch2ps075.talenthubmitra.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ch2ps075.talenthubmitra.data.repo.AuthRepository
import com.ch2ps075.talenthubmitra.injection.Injection
import com.ch2ps075.talenthubmitra.ui.auth.login.LoginViewModel
import com.ch2ps075.talenthubmitra.ui.main.MainViewModel
import com.ch2ps075.talenthubmitra.ui.auth.register.RegisterViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository,
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(authRepository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(authRepository) as T
            }

            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(authRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(
                Injection.provideAuthRepository(context)
            )
        }.also { instance = it }
    }
}