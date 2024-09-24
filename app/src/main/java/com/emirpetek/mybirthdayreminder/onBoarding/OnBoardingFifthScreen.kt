package com.emirpetek.mybirthdayreminder.onBoarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.OnboardingFragmentFifthScreenBinding


class OnBoardingFifthScreen : Fragment() {

    private lateinit var binding: OnboardingFragmentFifthScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = OnboardingFragmentFifthScreenBinding.inflate(inflater,container,false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager2)


        binding.textViewOnBoardingFifthStart.setOnClickListener {
            findNavController().navigate(R.id.action_onBoardingFragment_to_loginFragment)
            onBoardingFinish()
        }



        // Inflate the layout for this fragment
        return binding.root
    }

    private fun onBoardingFinish(){
        val sp = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sp.edit()
        editor.putBoolean("finished",true)
        editor.apply()
    }

}