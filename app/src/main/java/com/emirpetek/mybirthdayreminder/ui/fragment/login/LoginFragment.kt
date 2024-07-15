package com.emirpetek.mybirthdayreminder.ui.fragment.login

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentLoginBinding
import com.emirpetek.mybirthdayreminder.viewmodel.login.LoginViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.math.round

class LoginFragment : Fragment() {



    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding:FragmentLoginBinding
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var editor: Editor
    private lateinit var auth: FirebaseAuth



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(inflater,container,false)

        binding.buttonLoginRegister.setOnClickListener{
            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_registerFragment)

        }
        auth = Firebase.auth

        hideBottomNav()


        mockEditText()

        binding.buttonLoginSignIn.setOnClickListener{
            loginUser()
        }


        return binding.root
    }

    private fun hideBottomNav(){
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav?.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun mockEditText(){
        binding.editTextLoginEmail.setText("emirpetek2002@gmail.com")
        binding.editTextLoginPassword.setText("emir2002")
    }

    private fun setRememberMe(){
        sharedPreferences = requireContext().getSharedPreferences("userAuthentication", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        editor.putBoolean("rememberMe",true)
        editor.commit()
    }

    private fun loginUser(){
        val email = binding.editTextLoginEmail.text.toString()
        val password = binding.editTextLoginPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()){ task ->
                if (task.isSuccessful){
                     toastShow(requireContext().getString(R.string.login_successfull))
                    findNavController().navigate(R.id.action_loginFragment_to_birthdaysFragment)
                    val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                    bottomNav?.visibility = View.VISIBLE
                }
            }







        }else{
            toastShow(requireContext().getString(R.string.fill_all_place))
        }
    }

    private fun toastShow(text:String){
        Toast.makeText(context,text, Toast.LENGTH_SHORT).show()
    }

}