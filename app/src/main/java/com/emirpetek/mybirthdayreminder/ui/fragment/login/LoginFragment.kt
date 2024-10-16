package com.emirpetek.mybirthdayreminder.ui.fragment.login

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.location.Location
import android.location.LocationManager
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.loginLogs.LogDetails
import com.emirpetek.mybirthdayreminder.data.entity.loginLogs.UserLoginLogs
import com.emirpetek.mybirthdayreminder.databinding.FragmentLoginBinding
import com.emirpetek.mybirthdayreminder.viewmodel.login.LoginViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.net.HttpURLConnection
import java.net.URL
import android.Manifest
import android.content.pm.PackageManager
import androidx.navigation.NavOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.round

class LoginFragment : Fragment() {


    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: FragmentLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.buttonLoginRegister.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_loginFragment_to_registerFragment)

        }
        auth = Firebase.auth

        hideBottomNav()


        checkRememberMe()

        binding.buttonLoginSignIn.setOnClickListener {
            loginUser()
        }

        binding.textViewLoginFragmentForgotPassword.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment) }


        return binding.root
    }

    private fun hideBottomNav() {
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun setRememberMe(email: String, password: String) {
        sharedPreferences =
            requireContext().getSharedPreferences("userAuthentication", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putBoolean("rememberMe", true)
        editor.putString("email", email)
        editor.putString("password", password)
        editor.commit()
        editor.apply()
    }

    private fun checkRememberMe(){
        sharedPreferences = requireContext().getSharedPreferences("userAuthentication", MODE_PRIVATE)

        val email = sharedPreferences.getString("email", null)  // E-posta
        val password = sharedPreferences.getString("password", null)  // Şifre

        if (email != null && password != null){
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            toastShow(requireContext().getString(R.string.login_successfull))
                            userLoginLogSave()
                            findNavController().navigate(R.id.action_loginFragment_to_birthdaysFragment)
                            val bottomNav =
                                activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                            bottomNav?.visibility = View.VISIBLE
                        }
                    }
            }
        }
    }

    private fun loginUser() {
        val email = binding.editTextLoginEmail.text.toString()
        val password = binding.editTextLoginPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        toastShow(requireContext().getString(R.string.login_successfull))
                        userLoginLogSave()
                        if (binding.checkBoxLoginFragmentRememberMe.isChecked) setRememberMe(email,password)
                        findNavController().navigate(
                            R.id.action_loginFragment_to_birthdaysFragment,
                            null,
                            NavOptions.Builder().setPopUpTo(R.id.loginFragment,true)
                                .build())
                        //findNavController().navigate(R.id.action_loginFragment_to_birthdaysFragment)
                        val bottomNav =
                            activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                        bottomNav?.visibility = View.VISIBLE
                    }
                }
                .addOnFailureListener {
                    toastShow(requireContext().getString(R.string.check_email_password))
                }
        } else {
            toastShow(requireContext().getString(R.string.fill_all_place))
        }
    }

    private fun toastShow(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun userLoginLogSave() {




        val log = UserLoginLogs("", Firebase.auth.currentUser!!.uid, System.currentTimeMillis())

        viewModel.setLoginLog(log)
        CoroutineScope(Dispatchers.Main).launch{
            var ipAddress: String ?= null
            ipAddress = getPublicIpAddress()
            val logLong = LogDetails(
                System.currentTimeMillis(),
                ipAddress,
                auth.currentUser!!.uid
            )

            viewModel.addLogDataToList(logLong)
        }

    }

    suspend fun getPublicIpAddress(): String? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL("https://api.ipify.org?format=json")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use { it.readText() }
                // Parse JSON response to get the IP address
                val jsonResponse = org.json.JSONObject(response)
                jsonResponse.getString("ip")
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}