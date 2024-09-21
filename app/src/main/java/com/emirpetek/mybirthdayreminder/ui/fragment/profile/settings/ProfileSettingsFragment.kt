package com.emirpetek.mybirthdayreminder.ui.fragment.profile.settings

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentProfileSettingsBinding
import com.emirpetek.mybirthdayreminder.viewmodel.profile.ProfileSettingsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileSettingsFragment : Fragment() {

    private val viewModel: ProfileSettingsViewModel by viewModels()
    private lateinit var binding: FragmentProfileSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileSettingsBinding.inflate(inflater,container,false)

        hideBottomNav()

        binding.imageViewFragmentSettingsToolbar.setOnClickListener { findNavController().popBackStack() }
        binding.constraintLayoutSettingsAccount.setOnClickListener { findNavController().navigate(R.id.action_profileSettingsFragment_to_settingsAccountFragment) }
        binding.constraintLayoutSettingsLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(
                R.id.action_profileSettingsFragment_to_loginFragment,
                null,
                NavOptions.Builder().setPopUpTo(R.id.loginFragment,true)
                    .build())//navigate(R.id.action_profileSettingsFragment_to_loginFragment,)
        }

        binding.constraintLayoutSettingsPrivacy.setOnClickListener { showPrivacyDialog() }

        return binding.root
    }

    private fun hideBottomNav(){
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav?.visibility = View.GONE
    }

    fun showPrivacyDialog(){
        val ad = AlertDialog.Builder(requireContext())
        ad.setTitle(getString(R.string.privacy))
        ad.setMessage(getString(R.string.privacy_policy_text))
        ad.setPositiveButton("OK"){ dialog, it ->
            dialog.dismiss()
        }
        ad.create().show()
    }



}