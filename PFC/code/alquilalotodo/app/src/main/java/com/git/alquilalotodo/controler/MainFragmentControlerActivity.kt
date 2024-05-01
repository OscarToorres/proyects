package com.git.alquilalotodo.controler

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.git.alquilalotodo.R
import com.git.alquilalotodo.databinding.ActivityMainBinding
import com.git.alquilalotodo.ui.fragment.*
import com.git.alquilalotodo.ui.fragment.perfil.ProfileFragment
import com.google.firebase.auth.FirebaseAuth

class MainFragmentControlerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            binding.bottomNavigationview.setOnItemSelectedListener { item ->

                when (item.itemId) {

                    R.id.navigation_home -> replaceFragment(HomeFragment())
                    R.id.navigation_rent -> replaceFragment(CategoriesFragment())
                    R.id.navigation_favourites -> replaceFragment(FavoritesFragment())
                    R.id.navigation_profile -> replaceFragment(ProfileFragment())

                    else -> {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.fragmentError),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                true
            }
        } else {
            binding.bottomNavigationview.setOnItemSelectedListener { item ->

                when (item.itemId) {

                    R.id.navigation_home -> replaceFragment(HomeFragment())
                    R.id.navigation_rent -> replaceFragment(UnregisteredFragment())
                    R.id.navigation_favourites -> replaceFragment(UnregisteredFragment())
                    R.id.navigation_profile -> replaceFragment(UnregisteredFragment())

                    else -> {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.fragmentError),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                true
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_main, fragment)
        fragmentTransaction.commit()
    }
}