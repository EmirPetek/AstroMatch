package com.emirpetek.mybirthdayreminder.ui.fragment.profile.profileVisitors

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentProfileVisitorsBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.profile.profileVisitors.ProfileVisitorsAdapter
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility
import com.emirpetek.mybirthdayreminder.viewmodel.profile.ProfileVisitorsViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileVisitorsFragment : Fragment() {

    private val viewModel: ProfileVisitorsViewModel by viewModels()
    private lateinit var binding: FragmentProfileVisitorsBinding
    private lateinit var adapter: ProfileVisitorsAdapter
    private lateinit var mAdView : AdView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileVisitorsBinding.inflate(inflater,container,false)

        ManageBottomNavigationVisibility(requireActivity()).hideBottomNav()

        binding.recyclerViewVisitors.setHasFixedSize(true)
        binding.recyclerViewVisitors.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

        bindAdMob()

        binding.imageViewProfileVisitorsBackButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.progressBarProfileVisitors.visibility = View.VISIBLE
        viewModel.getVisitorList()
        viewModel.visitList.observe(viewLifecycleOwner, Observer { visitList ->
            val list = visitList.sortedByDescending { it.timestamp }
            adapter = ProfileVisitorsAdapter(requireContext(),list,binding.progressBarProfileVisitors)
            binding.recyclerViewVisitors.adapter = adapter

            if (visitList.isNullOrEmpty()){
                binding.progressBarProfileVisitors.visibility = View.GONE
                binding.textViewProfileVisitorsNoVisitHere.visibility = View.VISIBLE
            }
        })


        return binding.root
    }

    fun bindAdMob(){
        // birthdays sayfası reklam gösterme kısmı
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(requireContext()) {}
        }
        mAdView = binding.adViewProfileVisitors
        val adView = AdView(requireContext())
        adView.adUnitId = getString(R.string.ad_unit_id)
        val adSize = AdSize(LayoutParams.MATCH_PARENT,80)
        adView.setAdSize(adSize)
        this.mAdView = adView
        binding.adViewProfileVisitors.removeAllViews()
        binding.adViewProfileVisitors.addView(adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
}