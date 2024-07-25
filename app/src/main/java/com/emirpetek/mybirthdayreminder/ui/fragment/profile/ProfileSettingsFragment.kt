package com.emirpetek.mybirthdayreminder.ui.fragment.profile

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentProfileSettingsBinding
import com.emirpetek.mybirthdayreminder.viewmodel.profile.ProfileSettingsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

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


        return binding.root
    }

    private fun hideBottomNav(){
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav?.visibility = View.GONE
    }



}