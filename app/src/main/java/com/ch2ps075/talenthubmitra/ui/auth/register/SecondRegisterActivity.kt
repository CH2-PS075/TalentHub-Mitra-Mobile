package com.ch2ps075.talenthubmitra.ui.auth.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ch2ps075.talenthubmitra.R
import com.ch2ps075.talenthubmitra.databinding.ActivitySecondRegisterBinding
import com.ch2ps075.talenthubmitra.ui.auth.register.FirstRegisterActivity.Companion.ADDRESS
import com.ch2ps075.talenthubmitra.ui.auth.register.FirstRegisterActivity.Companion.CATEGORY
import com.ch2ps075.talenthubmitra.ui.auth.register.FirstRegisterActivity.Companion.CONTACT
import com.ch2ps075.talenthubmitra.ui.auth.register.FirstRegisterActivity.Companion.EMAIL
import com.ch2ps075.talenthubmitra.ui.auth.register.FirstRegisterActivity.Companion.GROUP_TYPE
import com.ch2ps075.talenthubmitra.ui.auth.register.FirstRegisterActivity.Companion.PASSWORD
import com.ch2ps075.talenthubmitra.ui.auth.register.FirstRegisterActivity.Companion.PRICE
import com.ch2ps075.talenthubmitra.ui.auth.register.FirstRegisterActivity.Companion.TALENT_NAME

class SecondRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            nextRegister2Button.setOnClickListener {
                when {
                    edRegisterEmail.text.toString().isEmpty() -> {
                        edRegisterEmail.error = getString(R.string.error_empty_field)
                    }

                    edRegisterPassword.text.toString().length < 8 -> {
                        edRegisterPassword.error = getString(R.string.error_short_password)
                    }

                    else -> getBundleFromFirstRegisterActivity()
                }
            }
        }
    }

    private fun getBundleFromFirstRegisterActivity() {
        val getBundle = intent.extras
        val talentName = getBundle?.getString(TALENT_NAME)
        val address = getBundle?.getString(ADDRESS)
        val contact = getBundle?.getString(CONTACT)
        val price = getBundle?.getString(PRICE)
        val category = getBundle?.getString(CATEGORY)
        val groupType = getBundle?.getString(GROUP_TYPE)
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()

        val intent = Intent(this, ThirdRegisterActivity::class.java)
        val bundle = Bundle()

        bundle.putString(TALENT_NAME, talentName)
        bundle.putString(ADDRESS, address)
        bundle.putString(CONTACT, contact)
        bundle.putString(PRICE, price)
        bundle.putString(CATEGORY, category)
        bundle.putString(GROUP_TYPE, groupType)
        bundle.putString(EMAIL, email)
        bundle.putString(PASSWORD, password)

        intent.putExtras(bundle)
        startActivity(intent)
    }
}