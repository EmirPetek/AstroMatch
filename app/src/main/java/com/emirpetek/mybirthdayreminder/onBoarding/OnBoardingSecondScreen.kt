package com.emirpetek.mybirthdayreminder.onBoarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.OnboardingFragmentSecondScreenBinding


class OnBoardingSecondScreen : Fragment() {

    private lateinit var binding: OnboardingFragmentSecondScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = OnboardingFragmentSecondScreenBinding.inflate(inflater,container,false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager2)

        binding.textViewOnBoardingSecondNext.setOnClickListener {
            viewPager?.currentItem = 2
        }


        return binding.root
    }

}