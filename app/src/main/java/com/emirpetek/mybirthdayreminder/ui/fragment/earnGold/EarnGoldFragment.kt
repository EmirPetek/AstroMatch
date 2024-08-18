package com.emirpetek.mybirthdayreminder.ui.fragment.earnGold

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility
import com.emirpetek.mybirthdayreminder.databinding.FragmentEarnGoldBinding
import com.emirpetek.mybirthdayreminder.viewmodel.earnGold.EarnGoldViewModel

class EarnGoldFragment : Fragment() {

    private val viewModel: EarnGoldViewModel by viewModels()
    private lateinit var binding: FragmentEarnGoldBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEarnGoldBinding.inflate(inflater,container,false)

        ManageBottomNavigationVisibility(requireActivity()).hideBottomNav()

        binding.cardEarnGoldAdFirst.setOnClickListener { viewModel.incrementUserCredit(1) }
        binding.cardEarnGoldAdSecond.setOnClickListener { viewModel.incrementUserCredit(5) }

        binding.cardBuyGold1.setOnClickListener { viewModel.incrementUserCredit(50) }
        binding.cardBuyGold2.setOnClickListener { viewModel.incrementUserCredit(100) }
        binding.cardBuyGold3.setOnClickListener { viewModel.incrementUserCredit(200) }
        binding.cardBuyGold4.setOnClickListener { viewModel.incrementUserCredit(500) }
        binding.cardBuyGold5.setOnClickListener { viewModel.incrementUserCredit(1000) }
        binding.cardBuyGold6.setOnClickListener { viewModel.incrementUserCredit(5000) }

        return binding.root
    }
}