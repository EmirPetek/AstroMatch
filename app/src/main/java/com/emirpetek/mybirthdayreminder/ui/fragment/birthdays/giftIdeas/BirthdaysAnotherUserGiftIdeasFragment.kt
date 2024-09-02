package com.emirpetek.mybirthdayreminder.ui.fragment.birthdays.giftIdeas

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.birthdays.BirthdayGiftIdea
import com.emirpetek.mybirthdayreminder.databinding.FragmentBirthdaysAnotherUserGiftIdeasBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.birthdays.giftIdeas.BirthdaysAnotherUserGiftIdeaBottomBarAdapter
import com.emirpetek.mybirthdayreminder.ui.adapter.birthdays.giftIdeas.BirthdaysAnotherUserGiftIdeasAdapter
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility
import com.emirpetek.mybirthdayreminder.viewmodel.birthdays.BirthdaysAnotherUserGiftIdeasViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.rvadapter.AdmobNativeAdAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BirthdaysAnotherUserGiftIdeasFragment : Fragment() {


    private val viewModel: BirthdaysAnotherUserGiftIdeasViewModel by viewModels()
    private lateinit var binding: FragmentBirthdaysAnotherUserGiftIdeasBinding
    private lateinit var adapter: BirthdaysAnotherUserGiftIdeasAdapter
    private lateinit var bottomBarAdapter : BirthdaysAnotherUserGiftIdeaBottomBarAdapter
    lateinit var admobNativeAdAdapter: AdmobNativeAdAdapter
    private var filteredItems : List<Int> = arrayListOf()
    private var newIdea: List<BirthdayGiftIdea> = listOf()
    private lateinit var mAdView : AdView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBirthdaysAnotherUserGiftIdeasBinding.inflate(inflater,container,false)

        ManageBottomNavigationVisibility(requireActivity()).hideBottomNav()

        binding.progressBarAnotherUserGiftIdea.visibility = View.VISIBLE

        binding.imageViewAnotherUserGiftIdeasBackButton.setOnClickListener { findNavController().popBackStack() }

        bindAdMob()


        viewModel.getGiftIdeas()
        filterItems(filteredItems)

        binding.imageViewBirthdaysAnotherGiftIdeaFilter.setOnClickListener { setupBottomSheetDialog() }


        return binding.root
    }

    private fun setupBottomSheetDialog(){
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_select_item_text,null)
        val rv : RecyclerView = view.findViewById(R.id.recyclerViewBottomSheetSelectItemText)
        val textViewApply : TextView = view.findViewById(R.id.textViewBottomSheetSelectItemApply)

        val btnClose = view.findViewById<ImageView>(R.id.imageViewBottomSheetSelectItemTextCloseButton)
        btnClose.setOnClickListener { dialog.dismiss() }

        rv.setHasFixedSize(true)
        rv.layoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.HORIZONTAL)

        val family = getString(R.string.family)
        val friend = getString(R.string.friends)
        val bros = getString(R.string.bros)
        val colleagues = getString(R.string.colleagues)
        val acquaintances = getString(R.string.acquaintances)
        val partner = getString(R.string.partner)
        val vip = getString(R.string.vip)
        val userDegrees  = arrayListOf(family,friend,bros,colleagues,acquaintances,partner,vip)
        bottomBarAdapter = BirthdaysAnotherUserGiftIdeaBottomBarAdapter(requireContext(),userDegrees,filteredItems)
        rv.adapter = bottomBarAdapter



        bottomBarAdapter.setOnItemClickListener(object : BirthdaysAnotherUserGiftIdeaBottomBarAdapter.OnItemClickListener{
            override fun onItemClicked(selectedItems: List<Int>) {
                textViewApply.setOnClickListener { it ->
                    filterItems(selectedItems)
                    dialog.dismiss()
                }
            }
        })


        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun filterItems(list: List<Int>){

        viewModel.giftIdeaList.observe(viewLifecycleOwner, Observer { idea ->
            binding.recyclerViewAnotherUserGiftIdeas.setHasFixedSize(true)
            binding.recyclerViewAnotherUserGiftIdeas.layoutManager = LinearLayoutManager(requireContext())

            if (list.isEmpty()){
                adapter = BirthdaysAnotherUserGiftIdeasAdapter(requireContext(),idea,binding.progressBarAnotherUserGiftIdea)
                binding.textViewBirthdaysAnotherUserGiftIdeaNoIdea.visibility = View.GONE

            }
            else{ // bottom sheet dialogdan herhangi bir item seçilmemişse
                filteredItems = list//.sortedBy { it }
                newIdea = idea.filter { it.userDegree in list }
                if (newIdea.isEmpty()) binding.textViewBirthdaysAnotherUserGiftIdeaNoIdea.visibility = View.VISIBLE
                else binding.textViewBirthdaysAnotherUserGiftIdeaNoIdea.visibility = View.GONE
                adapter = BirthdaysAnotherUserGiftIdeasAdapter(requireContext(),newIdea,binding.progressBarAnotherUserGiftIdea)
            }


            admobNativeAdAdapter = AdmobNativeAdAdapter.Builder.with(
                getString(R.string.ad_native_id),
                adapter,
                "small")
                .adItemInterval(10)
                .build()

            binding.recyclerViewAnotherUserGiftIdeas.adapter = admobNativeAdAdapter

        })
    }

    fun bindAdMob(){
        // comelikes sayfası reklam gösterme kısmı
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(requireContext()) {}
        }
        mAdView = binding.adViewGiftIdeasFragment
        val adView = AdView(requireContext())
        adView.adUnitId = getString(R.string.ad_unit_id)
        val adSize = AdSize(LayoutParams.MATCH_PARENT, 60)
        adView.setAdSize(adSize)
        this.mAdView = adView
        binding.adViewGiftIdeasFragment.removeAllViews()
        binding.adViewGiftIdeasFragment.addView(adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

}