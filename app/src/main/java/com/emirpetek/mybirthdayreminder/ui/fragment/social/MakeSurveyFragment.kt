package com.emirpetek.mybirthdayreminder.ui.fragment.social

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentMakeSurveyBinding
import com.emirpetek.mybirthdayreminder.viewmodel.social.MakeSurveyViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MakeSurveyFragment : Fragment() {

    private val viewModel: MakeSurveyViewModel by viewModels()
    private lateinit var binding: FragmentMakeSurveyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMakeSurveyBinding.inflate(inflater,container,false)
        binding.fragmentObjMakeSurvey = this
        hideBottomNav()


        return binding.root
    }

    private fun hideBottomNav(){
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav?.visibility = View.GONE
    }

    fun previousPage(view: View){
        findNavController().popBackStack()
    }
}