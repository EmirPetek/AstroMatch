package com.emirpetek.mybirthdayreminder.ui.util.showPhotos

import android.graphics.Rect
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentShowPhotosBinding
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShowPhotosFragment : Fragment() {


    private val viewModel: ShowPhotosViewModel by viewModels()
    private lateinit var binding: FragmentShowPhotosBinding
    private lateinit var adapter: ShowPhotosAdapter
    lateinit var imageList : ArrayList<String>
    var imageIndex : Int = 0
    private lateinit var mAdView : AdView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowPhotosBinding.inflate(inflater,container,false)

        imageList = arguments?.getStringArrayList("imageList")!!
        Log.e("imagelist: ", imageList.toString())

        imageIndex = arguments?.getInt("imageIndex")!!

        ManageBottomNavigationVisibility(requireActivity()).hideBottomNav()
        setAdView()
        binding.imageViewShowPhotosGoBack.setOnClickListener { goBack() }

        binding.textViewShowPhotosPhotoOrder.text = "1/${imageList.size}"
        setRecyclerView()


        return binding.root
    }

    fun goBack(){
        findNavController().popBackStack()
    }

    fun setAdView(){
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(requireContext()) {}
        }
        mAdView = binding.adViewShowPhotos
        val adView = AdView(requireContext())
        adView.adUnitId = getString(R.string.ad_unit_id)
        val adSize = AdSize(400,50)
        adView.setAdSize(adSize)
        this.mAdView = adView
        binding.adViewShowPhotos.removeAllViews()
        binding.adViewShowPhotos.addView(adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

    }

    fun setRecyclerView(){

        binding.recyclerViewShowPhotos.setHasFixedSize(true)
        binding.recyclerViewShowPhotos.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        adapter = ShowPhotosAdapter(requireContext(),imageList)
        val snapHelper: SnapHelper = LinearSnapHelper()

        val layoutManager = binding.recyclerViewShowPhotos.layoutManager as LinearLayoutManager
        layoutManager.scrollToPositionWithOffset(imageIndex, 0)

        binding.recyclerViewShowPhotos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val rvLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val snapView = snapHelper.findSnapView(rvLayoutManager)
                    val snapPosition = rvLayoutManager.getPosition(snapView!!)

                    // Pozisyonu güncelle
                    val adapterPosition = snapPosition + 1
                    val listSize = imageList.size
                    val positionText = "$adapterPosition/$listSize"
                    binding.textViewShowPhotosPhotoOrder.text = positionText
                }
            }
        })


        snapHelper.attachToRecyclerView(binding.recyclerViewShowPhotos)

        binding.recyclerViewShowPhotos.adapter = adapter


    }
}