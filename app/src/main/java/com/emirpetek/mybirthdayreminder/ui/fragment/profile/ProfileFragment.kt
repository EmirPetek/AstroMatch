package com.emirpetek.mybirthdayreminder.ui.fragment.profile

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentProfileBinding
import com.emirpetek.mybirthdayreminder.viewmodel.profile.ProfileViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container,false)

        auth = Firebase.auth
        showBottomNav()

        Glide.with(this)
            .load("https://www.bio.purdue.edu/lab/deng/images/photo_not_yet_available.jpg")
            .circleCrop()
            .into(binding.imageViewProfilePhoto)


        viewModel.getUser(auth.currentUser!!.uid)
        viewModel.user.observe(viewLifecycleOwner, Observer { it ->
            val joined_at = context?.getString(R.string.joined_at)  + " " + formatTimestampToDate(it.created_at)
            val fullname = context?.getString(R.string.fullname)  + " " + it.fullname
            val birthdate = context?.getString(R.string.birthdate)  + " " + it.birthdate
            val zodiac = it.zodiac
            val ascendant = it.ascendant


            binding.textViewProfileFullname.setText(fullname)
            binding.textViewProfileBirthdate.setText(birthdate)
            binding.textViewProfileZodiac.setText(zodiac)
            binding.textViewProfileAscendant.setText(ascendant)
            binding.textViewProfileJoinedAt.setText(joined_at)


            bindZodiacAscendantImage(zodiac,ascendant)
        })

        binding.imageViewProfileFragmentSettings.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_profileSettingsFragment)
        }


        return binding.root
    }

    private fun showBottomNav(){
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav?.visibility = View.VISIBLE
    }

    private fun bindZodiacAscendantImage(zodiac:String,ascendant:String){
        val zodiacDrawableResId = when (zodiac.lowercase()) {
            getString(R.string.capricorn).lowercase() -> R.drawable.capricorn
            getString(R.string.aquarius).lowercase() -> R.drawable.aquarius
            getString(R.string.pisces).lowercase() -> R.drawable.pisces
            getString(R.string.aries).lowercase() -> R.drawable.aries
            getString(R.string.taurus).lowercase() -> R.drawable.taurus
            getString(R.string.gemini).lowercase() -> R.drawable.gemini
            getString(R.string.cancer).lowercase() -> R.drawable.cancer
            getString(R.string.leo).lowercase() -> R.drawable.leo
            getString(R.string.virgo).lowercase() -> R.drawable.virgo
            getString(R.string.libra).lowercase() -> R.drawable.libra
            getString(R.string.scorpio).lowercase() -> R.drawable.scorpio
            getString(R.string.sagittarius).lowercase() -> R.drawable.sagittarius
            else -> R.drawable.baseline_error_24 // Varsayılan resim
        }

        val ascendantDrawableResId = when (ascendant.lowercase()) {
            getString(R.string.capricorn).lowercase() -> R.drawable.capricorn
            getString(R.string.aquarius).lowercase() -> R.drawable.aquarius
            getString(R.string.pisces).lowercase() -> R.drawable.pisces
            getString(R.string.aries).lowercase() -> R.drawable.aries
            getString(R.string.taurus).lowercase() -> R.drawable.taurus
            getString(R.string.gemini).lowercase() -> R.drawable.gemini
            getString(R.string.cancer).lowercase() -> R.drawable.cancer
            getString(R.string.leo).lowercase() -> R.drawable.leo
            getString(R.string.virgo).lowercase() -> R.drawable.virgo
            getString(R.string.libra).lowercase() -> R.drawable.libra
            getString(R.string.scorpio).lowercase() -> R.drawable.scorpio
            getString(R.string.sagittarius).lowercase() -> R.drawable.sagittarius
            else -> R.drawable.baseline_error_24 // Varsayılan resim
        }

        Glide.with(this)
            .load(zodiacDrawableResId)
            .into(binding.imageViewProfileZodiac)

        Glide.with(this)
            .load(ascendantDrawableResId)
            .into(binding.imageViewProfileAscendant)
    }

    fun formatTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }
}