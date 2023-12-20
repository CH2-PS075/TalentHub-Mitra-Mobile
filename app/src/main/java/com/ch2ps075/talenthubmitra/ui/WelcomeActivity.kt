package com.ch2ps075.talenthubmitra.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ch2ps075.talenthubmitra.databinding.ActivityWelcomeBinding
import com.ch2ps075.talenthubmitra.ui.login.LoginActivity
import com.ch2ps075.talenthubmitra.ui.register.PreRegisterActivity
import com.ch2ps075.talenthubmitra.ui.register.VerifyPhotoActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startLoginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.startRegisterButton.setOnClickListener {
            startActivity(Intent(this, VerifyPhotoActivity::class.java))
        }
    }
}