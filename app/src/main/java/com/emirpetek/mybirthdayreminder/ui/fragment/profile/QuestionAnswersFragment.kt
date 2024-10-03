package com.emirpetek.mybirthdayreminder.ui.fragment.profile

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.apiKey.getAdmobApiKey
import com.emirpetek.mybirthdayreminder.data.entity.question.Post
import com.emirpetek.mybirthdayreminder.data.entity.question.QuestionAnswers
import com.emirpetek.mybirthdayreminder.databinding.FragmentQuestionAnswersBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.profile.QuestionAnswersAnswerAdapter
import com.emirpetek.mybirthdayreminder.ui.adapter.social.main.image.SocialPostImageAdapter
import com.emirpetek.mybirthdayreminder.ui.util.calculateTime.CalculateShareTime
import com.emirpetek.mybirthdayreminder.viewmodel.profile.QuestionAnswersViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.rvadapter.AdmobNativeAdAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestionAnswersFragment : Fragment() {


    private val viewModel: QuestionAnswersViewModel by viewModels()
    private lateinit var binding: FragmentQuestionAnswersBinding
    lateinit var admobNativeAdAdapter: AdmobNativeAdAdapter
    private lateinit var mAdView : AdView
    lateinit var answerList: ArrayList<QuestionAnswers> //= ArrayList<QuestionAnswers>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionAnswersBinding.inflate(inflater,container,false)

        hideBottomNav()
        binding.textViewFragmentQuestionAnswerNoPostHere.visibility = View.VISIBLE


        binding.imageViewFragmentQuestionAnswersToolbar.setOnClickListener { findNavController().popBackStack() }

        val post: Post? = arguments?.getParcelable("post")
        post?.let {
            bindPostData(post)
            binding.imageViewQuestionAnswersDeletePost.setOnClickListener { deletePost(post.postID,it) }

        }


        // birthdays sayfası banner reklam gösterme kısmı
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(requireContext()) {}
        }
        mAdView = binding.adViewQuestionAnswers
        val adView = AdView(requireContext())
        adView.adUnitId = getAdmobApiKey().getBannerAdmobApiKey()
        val adSize = AdSize(320,50)
        adView.setAdSize(adSize)
        this.mAdView = adView
        binding.adViewQuestionAnswers.removeAllViews()
        binding.adViewQuestionAnswers.addView(adView)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)


        setAnswerAdapterClass(post!!.postID)



        return binding.root
    }

    private fun hideBottomNav(){
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav?.visibility = View.GONE
    }

    private fun deletePost(postID: String, view: View){

        Snackbar.make(view,getString(R.string.are_you_sure_to_delete), Snackbar.LENGTH_SHORT)
            .setAction(getString(R.string.delete)){
                viewModel.deletePost(postID)
                findNavController().popBackStack()
            }.show()
    }


    private fun bindPostData(post: Post?){
        binding.textViewCardQuestionAnswersUserFullname.setText(post!!.userFullname)
        binding.textViewCardQuestionAnswersShareTime.setText(CalculateShareTime(requireContext()).unixtsToDate(post.timestamp.toString()))
        binding.textViewQuestionAnswersQuestionText.setText(post.questionText)

        val photoUri = post.userImg
        var loadUri = ""

        loadUri = if(photoUri.equals("no_photo")){
            "https://www.bio.purdue.edu/lab/deng/images/photo_not_yet_available.jpg"
        }else{
            photoUri.toString()
        }

        Glide.with(this)
            .load(loadUri)
            .circleCrop()
            .into(binding.imageViewCardQuestionAnswersProfileImg)

        if (post.imageURL[0].equals("null")){ binding.constraintLayoutQuestionAnswersPhoto.visibility = View.GONE }
        else{
            var imgList : ArrayList<String> = arrayListOf()
            imgList = (post.imageURL)

            binding.recyclerViewFragmentQuestionAnswerPostPhotoRecyclerView.setHasFixedSize(true)
            binding.recyclerViewFragmentQuestionAnswerPostPhotoRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            val imgAdapter = SocialPostImageAdapter(requireContext(),imgList,"posts/askQuestionPhoto","QuestionAnswersFragment")
            binding.recyclerViewFragmentQuestionAnswerPostPhotoRecyclerView.adapter = imgAdapter
        }

    }

    private fun setAnswerAdapterClass(postID: String) {

       // var answerList = ArrayList<QuestionAnswers>()

        viewModel.getAnswers(postID)
        viewModel.answerList.observe(viewLifecycleOwner, Observer { it ->
            answerList = it as ArrayList<QuestionAnswers>

            if (answerList.size > 0) binding.textViewFragmentQuestionAnswerNoPostHere.visibility = View.GONE

            binding.recyclerViewFragmentQuestionAnswerAnswersRecyclerView.setHasFixedSize(true)
            binding.recyclerViewFragmentQuestionAnswerAnswersRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            val answerAdapter = QuestionAnswersAnswerAdapter(requireContext(),answerList,viewModel)

            // NATIVE REKLAM İÇİN GEREKEN KODLAR. GITHUB FORKLADIM. ORADAN BAK. GEREKLI DEPENDENCIESLERI SYNC ETMEN LAZIM
            admobNativeAdAdapter = AdmobNativeAdAdapter.Builder.with(
                getAdmobApiKey().getNativeAdmobApiKey(),
                answerAdapter,
                "small")
                .adItemInterval(3)
                .build()

            binding.recyclerViewFragmentQuestionAnswerAnswersRecyclerView.adapter = admobNativeAdAdapter
        })

    }
}