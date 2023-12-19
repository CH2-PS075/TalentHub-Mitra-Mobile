package com.ch2ps075.talenthubmitra.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ch2ps075.talenthubmitra.R
import com.ch2ps075.talenthubmitra.databinding.ActivityMainBinding
import com.ch2ps075.talenthubmitra.ui.ViewModelFactory
import com.ch2ps075.talenthubmitra.ui.home.HomeFragment
import com.ch2ps075.talenthubmitra.ui.profile.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()

        setCurrentFragment(homeFragment)

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_home -> {
                    setCurrentFragment(homeFragment)
                    binding.bottomNavigation.menu.findItem(R.id.action_home)
                        ?.changeIcon(R.drawable.ic_home_active)
                    binding.bottomNavigation.menu.findItem(R.id.action_profile)
                        ?.changeIcon(R.drawable.ic_profile)
                }

                R.id.action_profile -> {
                    setCurrentFragment(profileFragment)
                    binding.bottomNavigation.menu.findItem(R.id.action_home)
                        ?.changeIcon(R.drawable.ic_home)
                    binding.bottomNavigation.menu.findItem(R.id.action_profile)
                        ?.changeIcon(R.drawable.ic_profile_active)
                }
            }
            true
        }

        checkUserLogin()
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_fragment, fragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun MenuItem.changeIcon(drawableId: Int) {
        this.icon = ContextCompat.getDrawable(this@MainActivity, drawableId)
    }

    private fun checkUserLogin() {
        viewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                Toast.makeText(this, "Selamat datang di TalentHub Mitra!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}