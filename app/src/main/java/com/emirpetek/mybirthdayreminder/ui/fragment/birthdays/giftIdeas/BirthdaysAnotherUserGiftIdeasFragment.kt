package com.emirpetek.mybirthdayreminder.ui.fragment.birthdays.giftIdeas

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentBirthdaysAnotherUserGiftIdeasBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.birthdays.giftIdeas.BirthdaysAnotherUserGiftIdeasAdapter
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility
import com.google.rvadapter.AdmobNativeAdAdapter

class BirthdaysAnotherUserGiftIdeasFragment : Fragment() {


    private val viewModel: BirthdaysAnotherUserGiftIdeasViewModel by viewModels()
    private lateinit var binding: FragmentBirthdaysAnotherUserGiftIdeasBinding
    private lateinit var adapter: BirthdaysAnotherUserGiftIdeasAdapter
    lateinit var admobNativeAdAdapter: AdmobNativeAdAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBirthdaysAnotherUserGiftIdeasBinding.inflate(inflater,container,false)

        ManageBottomNavigationVisibility(requireActivity()).hideBottomNav()

        binding.progressBarAnotherUserGiftIdea.visibility = View.VISIBLE

        binding.imageViewAnotherUserGiftIdeasBackButton.setOnClickListener { findNavController().popBackStack() }

        viewModel.getGiftIdeas()
        viewModel.giftIdeaList.observe(viewLifecycleOwner, Observer { idea ->
            binding.recyclerViewAnotherUserGiftIdeas.setHasFixedSize(true)
            binding.recyclerViewAnotherUserGiftIdeas.layoutManager = LinearLayoutManager(requireContext())
            adapter = BirthdaysAnotherUserGiftIdeasAdapter(requireContext(),idea,binding.progressBarAnotherUserGiftIdea)
            admobNativeAdAdapter = AdmobNativeAdAdapter.Builder.with(
                getString(R.string.ad_native_id),
                adapter,
                "small")
                .adItemInterval(10)
                .build()

            binding.recyclerViewAnotherUserGiftIdeas.adapter = admobNativeAdAdapter

        })


        return binding.root
    }
}