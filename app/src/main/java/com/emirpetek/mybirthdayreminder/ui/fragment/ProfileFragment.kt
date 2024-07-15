package com.emirpetek.mybirthdayreminder.ui.fragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentProfileBinding
import com.emirpetek.mybirthdayreminder.viewmodel.ProfileViewModel

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container,false)

        Glide.with(this)
            .load("https://www.bio.purdue.edu/lab/deng/images/photo_not_yet_available.jpg")
            .circleCrop()
            .into(binding.imageViewProfilePhoto)


        return binding.root
    }
}