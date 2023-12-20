package com.ch2ps075.talenthubmitra.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ch2ps075.talenthubmitra.R
import com.ch2ps075.talenthubmitra.databinding.FragmentHomeBinding
import com.ch2ps075.talenthubmitra.ui.ViewModelFactory
import com.ch2ps075.talenthubmitra.ui.WelcomeActivity
import com.ch2ps075.talenthubmitra.ui.auth.login.LoginActivity
import com.ch2ps075.talenthubmitra.ui.main.MainViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<MainViewModel> { ViewModelFactory.getInstance(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        observeSession()
        return binding.root
    }

    private fun observeSession() {
        viewModel.getSession().observe(requireActivity()) { user ->
            binding.tvStateIsloginOrlogout.setOnClickListener {
                val targetClass = if (user.isLogin) {
                    viewModel.logout()
                    LoginActivity::class.java
                } else {
                    WelcomeActivity::class.java
                }
                startActivity(Intent(requireActivity(), targetClass))
            }

            val name = if (user.isLogin) user.email else "MITRA TALENTHUB!"
            val loginTextRes = if (user.isLogin) R.string.logout_text else R.string.login_text

            binding.tvName.text = resources.getString(R.string.account_name, name)
            binding.tvStateIsloginOrlogout.text = resources.getString(loginTextRes)
        }
    }
}