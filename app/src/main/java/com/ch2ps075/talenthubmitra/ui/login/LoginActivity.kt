package com.ch2ps075.talenthubmitra.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ch2ps075.talenthubmitra.ui.main.MainActivity
import com.ch2ps075.talenthubmitra.R
import com.ch2ps075.talenthubmitra.data.preference.TalentModel
import com.ch2ps075.talenthubmitra.databinding.ActivityLoginBinding
import com.ch2ps075.talenthubmitra.state.ResultState
import com.ch2ps075.talenthubmitra.ui.ViewModelFactory
import com.ch2ps075.talenthubmitra.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            loginButton.setOnClickListener {
                when {
                    edLoginEmail.text.toString().isEmpty() -> {
                        edLoginEmail.error = getString(R.string.error_empty_field)
                    }

                    edLoginPassword.text.toString().length < 8 -> {
                        edLoginPassword.error = getString(R.string.error_short_password)
                    }

                    else -> login()
                }
            }
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login() {
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()

        viewModel.login(email, password).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is ResultState.Loading -> {
                        showLoading(true)
                    }

                    is ResultState.Success -> {
                        viewModel.saveSession(
                            TalentModel(
                                username = result.data.name,
                                email = email,
                                token = result.data.token
                            )
                        )
                        showSuccessAlert()
                        showLoading(false)
                    }

                    is ResultState.Error -> {
                        showErrorAlert()
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun showSuccessAlert() {
        SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText(getString(R.string.success_title))
            .setContentText(getString(R.string.success_login_message))
            .setConfirmButton(getString(R.string.continue_title)) {
                startActivity(Intent(this, MainActivity::class.java))
            }
            .apply { setCanceledOnTouchOutside(false) }
            .show()
    }

    private fun showErrorAlert() {
        SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            .setTitleText(getString(R.string.failed_title))
            .setContentText(getString(R.string.failed_login_message))
            .setConfirmButton(getString(R.string.try_again_title)) {
                it.dismissWithAnimation()
            }
            .apply { setCanceledOnTouchOutside(false) }
            .show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}