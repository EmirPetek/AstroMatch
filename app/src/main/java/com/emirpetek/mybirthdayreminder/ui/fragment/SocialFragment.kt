package com.emirpetek.mybirthdayreminder.ui.fragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentSocialBinding
import com.emirpetek.mybirthdayreminder.viewmodel.SocialViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class SocialFragment : Fragment() {


    private val viewModel: SocialViewModel by viewModels()
    private lateinit var binding: FragmentSocialBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSocialBinding.inflate(inflater,container,false)

        binding.imageViewSocialFragmentSharePost.setOnClickListener { setupBottomSheetDialog(it) }

        return binding.root
    }

    private fun setupBottomSheetDialog(view: View){
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_share_post,null)
        val btnClose = view.findViewById<ImageView>(R.id.imageViewBottomSheetSharePostCloseButton)
        btnClose.setOnClickListener { dialog.dismiss() }
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }
}