package com.emirpetek.mybirthdayreminder.ui.fragment.social

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentSocialBinding
import com.emirpetek.mybirthdayreminder.viewmodel.social.SocialViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog

class SocialFragment : Fragment() {


    private val viewModel: SocialViewModel by viewModels()
    private lateinit var binding: FragmentSocialBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSocialBinding.inflate(inflater,container,false)

        showBottomNav()
        binding.imageViewSocialFragmentSharePost.setOnClickListener { setupBottomSheetDialog() }

        return binding.root
    }

    private fun setupBottomSheetDialog(){
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_select_two_items,null)
        view.findViewById<TextView>(R.id.textViewBottomSheetDialogSelectTwoItemsFirst).text = (getString(R.string.make_survey))
        view.findViewById<TextView>(R.id.textViewBottomSheetDialogSelectTwoItemsSecond).text = (getString(R.string.ask_question))

        val btnClose = view.findViewById<ImageView>(R.id.imageViewBottomSheetTwoItemsCloseButton)
        btnClose.setOnClickListener { dialog.dismiss() }

        val layout1 = view.findViewById<ConstraintLayout>(R.id.constraintLayoutBottomSheetDialogSelectTwoItemsFirst)
        val layout2 = view.findViewById<ConstraintLayout>(R.id.constraintLayoutBottomSheetDialogSelectTwoItemsSecond)
        layout1.setOnClickListener {
            findNavController().navigate(R.id.action_socialFragment_to_makeSurveyFragment)
            dialog.dismiss()
        }
        layout2.setOnClickListener {
            findNavController().navigate(R.id.action_socialFragment_to_askQuestionFragment)
            dialog.dismiss()
        }

        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun showBottomNav(){
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav?.visibility = View.VISIBLE
    }
}