package com.emirpetek.mybirthdayreminder.ui.fragment.profile.userGalleryPhotos

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.apiKey.getAdmobApiKey
import com.emirpetek.mybirthdayreminder.data.entity.user.UserGalleryPhoto
import com.emirpetek.mybirthdayreminder.databinding.FragmentProfileGalleryViewAllBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.profile.userGalleryPhotos.ProfileGalleryViewAllAdapter
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility
import com.emirpetek.mybirthdayreminder.viewmodel.profile.ProfileGalleryViewAllViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileGalleryViewAllFragment : Fragment() {

    private val viewModel: ProfileGalleryViewAllViewModel by viewModels()
    private lateinit var binding: FragmentProfileGalleryViewAllBinding
    private lateinit var adapter: ProfileGalleryViewAllAdapter
    lateinit var imageList : ArrayList<UserGalleryPhoto>
    private lateinit var mAdView : AdView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileGalleryViewAllBinding.inflate(inflater,container,false)

        imageList = arguments?.getParcelableArrayList("imageListProfileGallery")!!

        binding.imageViewProfileGallertGoButton.setOnClickListener { findNavController().popBackStack() }
        ManageBottomNavigationVisibility(requireActivity()).hideBottomNav()

        setAdview()
        binding.recyclerViewProfileGallery.setHasFixedSize(true)
      //  Log.e("imagelist: ", imageList.toString())
        binding.recyclerViewProfileGallery.layoutManager =
            //StaggeredGridLayoutManager(3,LinearLayoutManager.VERTICAL)
           GridLayoutManager(requireContext(),3)
        adapter = ProfileGalleryViewAllAdapter(requireContext(),imageList)


        binding.recyclerViewProfileGallery.adapter = adapter

        return binding.root
    }


    fun setAdview(){
        // birthdays sayfası banner reklam gösterme kısmı
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(requireContext()) {}
        }
        mAdView = binding.adViewFragmentProfileGalleryViewAll
        val adView = AdView(requireContext())
        adView.adUnitId = getAdmobApiKey().getUnitAdmobApiKey()
        val adSize = AdSize(400,50)
        adView.setAdSize(adSize)
        this.mAdView = adView
        binding.adViewFragmentProfileGalleryViewAll.removeAllViews()
        binding.adViewFragmentProfileGalleryViewAll.addView(adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }
}