package com.emirpetek.mybirthdayreminder.onBoarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.OnboardingFragmentFourthScreenBinding


class OnBoardingFourthScreen : Fragment() {

    private lateinit var binding:OnboardingFragmentFourthScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OnboardingFragmentFourthScreenBinding.inflate(inflater,container,false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager2)


        binding.textViewOnBoardingFourthNext.setOnClickListener {
            viewPager?.currentItem = 4
        }


        // Inflate the layout for this fragment
        return binding.root
    }

}