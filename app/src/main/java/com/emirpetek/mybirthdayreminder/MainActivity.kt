package com.emirpetek.mybirthdayreminder

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.ActivityMainBinding
import com.emirpetek.mybirthdayreminder.ui.fragment.BirthdaysFragment
import java.security.SecureRandom

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkOrSaveUser()
        loadFragment(BirthdaysFragment())

        /*binding.bottomNavigationBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.menuitem_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }

                R.id.menuitem_birthdays -> {
                    loadFragment(BirthdaysFragment())
                    true
                }

                else -> false
            }
        }*/
    }

    fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.mainactivity_constraint_layout,fragment)
        transaction.commit()
    }

    private fun checkOrSaveUser(){
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        val registerState: String = sharedPreferences.getString("registerState", "0")!!
        val userKeyData: String = sharedPreferences.getString("userKey", "0")!!

        if (registerState == "0") {
            registerUser()
        }
    }

    fun registerUser() {
        val sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        val userKey = generateRandomKey()
        editor.putString("userKey", userKey)
        editor.putString("registerState", "1")
        editor.commit()
    }

    fun generateRandomKey(): String {
        val characterSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val secureRandom = SecureRandom()

        // Random key will be created with 64 character
        val randomKeyBuilder = StringBuilder()
        for (i in 0..63) {
            val randomIndex = secureRandom.nextInt(characterSet.length)
            val randomChar = characterSet[randomIndex]
            randomKeyBuilder.append(randomChar)
        }
        return randomKeyBuilder.toString()
    }
}