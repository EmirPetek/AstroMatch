package com.emirpetek.mybirthdayreminder.ui.fragment.social.main

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.apiKey.getAdmobApiKey
import com.emirpetek.mybirthdayreminder.data.entity.question.Post
import com.emirpetek.mybirthdayreminder.databinding.FragmentSocialBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.social.main.post.SocialPostAdapter
import com.emirpetek.mybirthdayreminder.viewmodel.profile.ProfileViewModel
import com.emirpetek.mybirthdayreminder.viewmodel.social.AskQuestionViewModel
import com.emirpetek.mybirthdayreminder.viewmodel.social.MakeSurveyViewModel
import com.emirpetek.mybirthdayreminder.viewmodel.social.SocialViewModel
import com.google.android.gms.ads.AdLoader
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.rvadapter.AdmobNativeAdAdapter


class SocialFragment : Fragment() {


    private val viewModel: SocialViewModel by viewModels()
    private val viewModelSurvey: MakeSurveyViewModel by viewModels()
    private val viewModelQuestion: AskQuestionViewModel by viewModels()
    private val viewModelUser: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentSocialBinding
   // private var post: Post = Post()
    private var postList = ArrayList<Post>()// = arrayListOf()
    private lateinit var postAdapter : SocialPostAdapter
    lateinit var adLoader: AdLoader
    lateinit var admobNativeAdAdapter: AdmobNativeAdAdapter
    private var isNewCome: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSocialBinding.inflate(inflater,container,false)

        showBottomNav()
        binding.imageViewSocialFragmentSharePost.setOnClickListener {
            findNavController().navigate(R.id.action_socialFragment_to_askQuestionFragment)
            //setupBottomSheetDialog()
        }


        getPostData()



        return binding.root
    }

    override fun onStart() {
        super.onStart()
       // getPostData()

    }

    override fun onResume() {
        super.onResume()
        getPostData()
    }



    private fun getPostData(){


        binding.progressBarFragmentSocial.visibility = View.VISIBLE
        viewModelQuestion.getQuestionList()
        viewModelQuestion.questionList.observe(viewLifecycleOwner,Observer{ it ->
            postList = it as ArrayList<Post>
            setupPostItems(postList)

            // bu kod dbye yeni veri geldi mi sayfanın yenilenmesini önler
            viewModelQuestion.questionList.removeObservers(viewLifecycleOwner)

        })



      /*  viewModelSurvey.getSurveyList()
        viewModelSurvey.surveyList.observe(viewLifecycleOwner, Observer{ it ->
            //post.survey = it as ArrayList<Survey>
            postList += it as ArrayList<Post>
            setupPostItems()
        })*/


    }


   private fun setupPostItems(postList1: ArrayList<Post>) {

           postList1.sortByDescending { it.timestamp }


            for (i in this.postList){
               // Log.e("djfkfdlfa", i.postType)
            }
           binding.progressBarFragmentSocial.visibility = View.GONE
           binding.recyclerViewSocialFragment.setHasFixedSize(true)
           binding.recyclerViewSocialFragment.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
           postAdapter = SocialPostAdapter(requireContext(),
               postList1,viewModelQuestion,viewModelSurvey,viewLifecycleOwner,lifecycleScope,layoutInflater)

       // NATIVE REKLAM İÇİN GEREKEN KODLAR. GITHUB FORKLADIM. ORADAN BAK. GEREKLI DEPENDENCIESLERI SYNC ETMEN LAZIM
       admobNativeAdAdapter = AdmobNativeAdAdapter.Builder.with(
           getAdmobApiKey().getNativeAdmobApiKey(),
               postAdapter,
               "small")
           .adItemInterval(3)
           .build()

       binding.recyclerViewSocialFragment.adapter = admobNativeAdAdapter


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