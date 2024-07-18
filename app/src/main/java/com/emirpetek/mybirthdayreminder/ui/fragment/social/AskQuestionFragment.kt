package com.emirpetek.mybirthdayreminder.ui.fragment.social

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentAskQuestionBinding
import com.emirpetek.mybirthdayreminder.viewmodel.social.AskQuestionViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class AskQuestionFragment : Fragment() {

    private val viewModel: AskQuestionViewModel by viewModels()
    private lateinit var binding: FragmentAskQuestionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAskQuestionBinding.inflate(inflater,container,false)
        binding.fragmentObj = this

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