package com.emirpetek.mybirthdayreminder.ui.fragment.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentForgotPasswordBinding
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgotPasswordBinding
    private val auth: FirebaseAuth = Firebase.auth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgotPasswordBinding.inflate(inflater,container,false)

        ManageBottomNavigationVisibility(requireActivity()).hideBottomNav()

        binding.imageViewForgotPasswordBackButton.setOnClickListener { findNavController().popBackStack() }


        binding.buttonForgotPasswordSendEmail.setOnClickListener { sendForgotPasswordEmail() }

        return binding.root
    }

    fun sendForgotPasswordEmail(){
        val emailText = binding.editTextForgotPasswordEmail.text.toString()
        if (emailText.isEmpty()){
            Toast.makeText(requireContext(),getString(R.string.enter_email_to_reset),Toast.LENGTH_SHORT).show()
        }else{
            auth.sendPasswordResetEmail(emailText).addOnSuccessListener {
                Toast.makeText(requireContext(),getString(R.string.reset_mail_sent),Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }.addOnFailureListener {
                Toast.makeText(requireContext(),getString(R.string.email_wrong),Toast.LENGTH_SHORT).show()
            }
        }
    }



}