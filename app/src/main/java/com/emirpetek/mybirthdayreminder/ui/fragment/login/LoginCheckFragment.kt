package com.emirpetek.mybirthdayreminder.ui.fragment.login

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.loginLogs.LogDetails
import com.emirpetek.mybirthdayreminder.data.entity.loginLogs.UserLoginLogs
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility
import com.emirpetek.mybirthdayreminder.viewmodel.login.LoginCheckViewModel
import com.emirpetek.mybirthdayreminder.viewmodel.login.LoginViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL


class LoginCheckFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var auth: FirebaseAuth
    private val viewModel : LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth = Firebase.auth
        checkRememberMe()
        ManageBottomNavigationVisibility(requireActivity()).hideBottomNav()

        return inflater.inflate(R.layout.fragment_login_check, container, false)
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
                            findNavController().navigate(
                                R.id.action_loginCheckFragment_to_birthdaysFragment,
                                null,
                                NavOptions.Builder().setPopUpTo(R.id.loginCheckFragment,true)
                                    .build())
                            //findNavController().navigate(R.id.action_loginCheckFragment_to_birthdaysFragment)
                        }else{
                            findNavController().navigate(R.id.action_loginCheckFragment_to_loginFragment)
                            toastShow(requireContext().getString(R.string.something_wrong_email_pw))
                        }
                    }
            }
        }else{
            findNavController().navigate(R.id.action_loginCheckFragment_to_loginFragment)
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