package com.ch2ps075.talenthubmitra.ui.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ch2ps075.talenthubmitra.databinding.ActivityPreRegisterBinding
import com.ch2ps075.talenthubmitra.ui.login.LoginActivity

class PreRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.continueButton.setOnClickListener { startActivity(Intent(this, RegisterActivity::class.java)) }
        binding.tvSignin.setOnClickListener { startActivity(Intent(this, LoginActivity::class.java)) }
    }
}