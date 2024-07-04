package com.emirpetek.mybirthdayreminder.ui.fragment.login

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentRegisterBinding
import com.emirpetek.mybirthdayreminder.viewmodel.RegisterViewModel

class RegisterFragment : Fragment() {

    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)

        binding.imageButtonSignUpBack.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }



        return binding.root
    }
}