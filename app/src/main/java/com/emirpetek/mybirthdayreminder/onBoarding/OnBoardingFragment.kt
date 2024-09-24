package com.emirpetek.mybirthdayreminder.onBoarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentOnBoardingBinding
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility

class OnBoardingFragment : Fragment() {

    private lateinit var binding:FragmentOnBoardingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnBoardingBinding.inflate(inflater,container,false)

        ManageBottomNavigationVisibility(requireActivity()).hideBottomNav()

        val fragmentList = arrayListOf<Fragment>(
            OnBoardingFirstScreen(),
            OnBoardingSecondScreen(),
            OnBoardingThirdScreen(),
            OnBoardingFourthScreen(),
            OnBoardingFifthScreen()
        )

        val adapter = ViewPagerAdapter(fragmentList,requireActivity().supportFragmentManager ,lifecycle)
        binding.viewPager2.adapter = adapter
        binding.dotsIndicator.attachTo(binding.viewPager2)

        // Inflate the layout for this fragment
        return binding.root
    }


}