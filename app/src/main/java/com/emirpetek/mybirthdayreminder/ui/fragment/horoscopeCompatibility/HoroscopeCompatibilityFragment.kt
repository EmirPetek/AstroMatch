package com.emirpetek.mybirthdayreminder.ui.fragment.horoscopeCompatibility

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentHoroscopeCompatibilityBinding
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility
import com.emirpetek.mybirthdayreminder.viewmodel.horoscopeCompatibility.HoroscopeCompatibilityViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HoroscopeCompatibilityFragment : Fragment() {

    private val viewModel: HoroscopeCompatibilityViewModel by viewModels()
    private lateinit var binding: FragmentHoroscopeCompatibilityBinding
    private lateinit var mAdView : AdView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHoroscopeCompatibilityBinding.inflate(inflater,container,false)

        ManageBottomNavigationVisibility(requireActivity()).hideBottomNav()
        binding.imageViewHoroscopeCompatibilityBackButton.setOnClickListener { findNavController().popBackStack() }
        bindAdMob()


        val input = "You are a helpful assistant, Role: System. Role: User. Make a detailed analysis of the horoscope compatibility of Ahmet, born on March 19, 1998, and Fatma, born on September 21, 2000. Analyze people according to their birth dates and personality according to birth dates. Let your comments be in your own language. The analysis should be at least 5-6 paragraphs. Give me analysis as Turkish language"

        binding.layoutCalculateCompatibility.setOnClickListener {
            Log.e("time: ", System.currentTimeMillis().toString())
            viewModel.getCompatibility(input)
            viewModel.compatibilityResponse.observe(viewLifecycleOwner, Observer { response ->
                Log.e("ai cevabı: ", response)
                Log.e("time 2: ", System.currentTimeMillis().toString())

            })
            Log.e("time 3: ", System.currentTimeMillis().toString())

        }

        return binding.root
    }

    fun bindAdMob(){
        // birthdays sayfası reklam gösterme kısmı
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(requireContext()) {}
        }
        mAdView = binding.adViewHoroscopeCompatibilityFragment
        val adView = AdView(requireContext())
        adView.adUnitId = getString(R.string.ad_unit_id)
        val adSize = AdSize(LayoutParams.MATCH_PARENT,80)
        adView.setAdSize(adSize)
        this.mAdView = adView
        binding.adViewHoroscopeCompatibilityFragment.removeAllViews()
        binding.adViewHoroscopeCompatibilityFragment.addView(adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
}