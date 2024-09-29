package com.emirpetek.mybirthdayreminder.ui.fragment.horoscopeCompatibility

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.navigation.fragment.findNavController
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.apiKey.getAdmobApiKey
import com.emirpetek.mybirthdayreminder.databinding.FragmentHoroscopeCompatibilityDetailBinding
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility
import com.emirpetek.mybirthdayreminder.viewmodel.horoscopeCompatibility.HoroscopeCompatibilityDetailViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HoroscopeCompatibilityDetailFragment : Fragment() {

    private val viewModel: HoroscopeCompatibilityDetailViewModel by viewModels()
    private lateinit var binding: FragmentHoroscopeCompatibilityDetailBinding
    private lateinit var mAdView : AdView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHoroscopeCompatibilityDetailBinding.inflate(inflater,container,false)

        ManageBottomNavigationVisibility(requireActivity()).hideBottomNav()

        binding.imageViewHoroscopeCompatibilityDetailBackButton.setOnClickListener { findNavController().popBackStack() }

        bindAdMob()
        val report = arguments?.getString("analysisDetail")

        binding.textViewHoroscopeCompatibilityDetailReport.text = report

        return binding.root
    }

    fun bindAdMob(){
        // birthdays sayfası reklam gösterme kısmı
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(requireContext()) {}
        }
        mAdView = binding.adViewHoroscopeCompatibiiltyDetail
        val adView = AdView(requireContext())
        adView.adUnitId = getAdmobApiKey().getUnitAdmobApiKey()
        val adSize = AdSize(LayoutParams.MATCH_PARENT,80)
        adView.setAdSize(adSize)
        this.mAdView = adView
        binding.adViewHoroscopeCompatibiiltyDetail.removeAllViews()
        binding.adViewHoroscopeCompatibiiltyDetail.addView(adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
}