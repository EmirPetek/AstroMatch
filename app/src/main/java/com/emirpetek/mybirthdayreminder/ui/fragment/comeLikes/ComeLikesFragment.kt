package com.emirpetek.mybirthdayreminder.ui.fragment.comeLikes

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
import androidx.recyclerview.widget.GridLayoutManager
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.apiKey.getAdmobApiKey
import com.emirpetek.mybirthdayreminder.data.entity.like.Like
import com.emirpetek.mybirthdayreminder.databinding.FragmentComeLikesBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.comeLikes.ComeLikesAdapter
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility
import com.emirpetek.mybirthdayreminder.viewmodel.comeLikes.ComeLikesViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ComeLikesFragment : Fragment() {

    private val viewModel: ComeLikesViewModel by viewModels()
    private lateinit var binding: FragmentComeLikesBinding
    private val ownUserID = Firebase.auth.currentUser!!.uid
    private lateinit var adapter : ComeLikesAdapter
    private lateinit var mAdView : AdView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentComeLikesBinding.inflate(inflater,container,false)

        ManageBottomNavigationVisibility(requireActivity()).hideBottomNav()
        binding.imageViewComeLikesBackButton.setOnClickListener { findNavController().popBackStack() }

        bindAdMob()

        binding.progressBarComeLikes.visibility = View.VISIBLE
        viewModel.getLikes(ownUserID)
        viewModel.getOwnUserData()
        viewModel.likeList.observe(viewLifecycleOwner, Observer { list ->
            val adapterList : List<Like> = listOf()
            binding.recyclerViewComeLikes.setHasFixedSize(true)
            binding.recyclerViewComeLikes.layoutManager = GridLayoutManager(requireContext(),2)
            viewModel.userData.observe(viewLifecycleOwner, Observer { ownUser ->
                val sortedList = list.sortedByDescending { it.type }
                adapter = ComeLikesAdapter(requireContext(), sortedList,ownUser,viewModel)
                binding.recyclerViewComeLikes.adapter = adapter
                binding.progressBarComeLikes.visibility = View.GONE
            })


            if (list.isEmpty()){
                binding.progressBarComeLikes.visibility = View.GONE
                binding.textViewComeLikesNoLikeHere.visibility = View.VISIBLE
            }
        })
        return binding.root
    }

    fun bindAdMob(){
        // comelikes sayfası reklam gösterme kısmı
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(requireContext()) {}
        }
        mAdView = binding.adViewComeLikes
        val adView = AdView(requireContext())
        adView.adUnitId = getAdmobApiKey().getUnitAdmobApiKey()
        val adSize = AdSize(LayoutParams.MATCH_PARENT, 60)
        adView.setAdSize(adSize)
        this.mAdView = adView
        binding.adViewComeLikes.removeAllViews()
        binding.adViewComeLikes.addView(adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
}