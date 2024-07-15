package com.emirpetek.mybirthdayreminder.ui.fragment.login

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentLoginBinding
import com.emirpetek.mybirthdayreminder.viewmodel.LoginViewModel

class LoginFragment : Fragment() {



    private val viewModel: LoginViewModel by viewModels()
    private lateinit var binding:FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(inflater,container,false)

        binding.buttonLoginRegister.setOnClickListener{ NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_registerFragment) }

        mockEditText()

        return binding.root
    }

    private fun mockEditText(){
        binding.editTextLoginEmail.setText("emirpetek2002@gmail.com")
        binding.editTextLoginPassword.setText("Emir2002")
    }
}