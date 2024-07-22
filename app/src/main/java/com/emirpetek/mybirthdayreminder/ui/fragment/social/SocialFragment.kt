package com.emirpetek.mybirthdayreminder.ui.fragment.social

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.Post
import com.emirpetek.mybirthdayreminder.data.entity.Question
import com.emirpetek.mybirthdayreminder.data.entity.Survey
import com.emirpetek.mybirthdayreminder.data.repo.social.SocialPostRepo
import com.emirpetek.mybirthdayreminder.databinding.FragmentSocialBinding
import com.emirpetek.mybirthdayreminder.viewmodel.social.AskQuestionViewModel
import com.emirpetek.mybirthdayreminder.viewmodel.social.MakeSurveyViewModel
import com.emirpetek.mybirthdayreminder.viewmodel.social.SocialViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SocialFragment : Fragment() {


    private val viewModel: SocialViewModel by viewModels()
    private val viewModelSurvey: MakeSurveyViewModel by viewModels()
    private val viewModelQuestion: AskQuestionViewModel by viewModels()
    private lateinit var binding: FragmentSocialBinding
   // private var post: Post = Post()
    private var postList : ArrayList<Post> = arrayListOf()
    private var surveyList : ArrayList<Post> = arrayListOf()
    private var questionList : ArrayList<Post> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSocialBinding.inflate(inflater,container,false)

        showBottomNav()
        binding.imageViewSocialFragmentSharePost.setOnClickListener { setupBottomSheetDialog() }

        getPostData()

        return binding.root
    }

    private fun getPostData(){
        viewModelQuestion.getQuestionList()
        viewModelQuestion.questionList.observe(viewLifecycleOwner,Observer{ it ->
            //post.question = it as ArrayList<Question>
            postList = it as ArrayList<Post>
        })

        viewModelSurvey.getSurveyList()
        viewModelSurvey.surveyList.observe(viewLifecycleOwner, Observer{ it ->
            //post.survey = it as ArrayList<Survey>
            postList = it as ArrayList<Post>
            setupPostItems()
        })
    }


   private fun setupPostItems() {

       for (p in postList){
           Log.e("post type: ", p.postType.toString())
           Log.e("post content: ", p.questionText.toString())
           Log.e("post ts: ", p.timestamp.toString())
       }
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