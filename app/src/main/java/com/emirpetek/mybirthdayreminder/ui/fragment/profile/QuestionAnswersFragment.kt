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
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.question.Post
import com.emirpetek.mybirthdayreminder.data.entity.question.QuestionAnswers
import com.emirpetek.mybirthdayreminder.databinding.FragmentQuestionAnswersBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.QuestionAnswersAnswerAdapter
import com.emirpetek.mybirthdayreminder.ui.adapter.social.main.SocialPostImageAdapter
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class QuestionAnswersFragment : Fragment() {


    private val viewModel: QuestionAnswersViewModel by viewModels()
    private lateinit var binding: FragmentQuestionAnswersBinding
    lateinit var admobNativeAdAdapter: AdmobNativeAdAdapter
    private lateinit var mAdView : AdView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionAnswersBinding.inflate(inflater,container,false)

        hideBottomNav()

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
        adView.adUnitId = getString(R.string.ad_unit_id)
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
        binding.textViewCardQuestionAnswersShareTime.setText(unixtsToDate(post.timestamp.toString()))
        binding.textViewQuestionAnswersQuestionText.setText(post.questionText)
        if (post.imageURL[0].equals("null")){ binding.constraintLayoutQuestionAnswersPhoto.visibility = View.GONE }
        else{
            var imgList : ArrayList<String> = arrayListOf()
            imgList = (post.imageURL)

            binding.recyclerViewFragmentQuestionAnswerPostPhotoRecyclerView.setHasFixedSize(true)
            binding.recyclerViewFragmentQuestionAnswerPostPhotoRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
            val imgAdapter = SocialPostImageAdapter(requireContext(),imgList,"posts/askQuestionPhoto")
            binding.recyclerViewFragmentQuestionAnswerPostPhotoRecyclerView.adapter = imgAdapter
        }
    }

    private fun setAnswerAdapterClass(postID: String) {





        var answerList = ArrayList<QuestionAnswers>()

        viewModel.getAnswers(postID)
        viewModel.answerList.observe(viewLifecycleOwner, Observer { it ->
            answerList = it as ArrayList<QuestionAnswers>

            binding.recyclerViewFragmentQuestionAnswerAnswersRecyclerView.setHasFixedSize(true)
            binding.recyclerViewFragmentQuestionAnswerAnswersRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
            val answerAdapter = QuestionAnswersAnswerAdapter(requireContext(),answerList,viewModel)

            // NATIVE REKLAM İÇİN GEREKEN KODLAR. GITHUB FORKLADIM. ORADAN BAK. GEREKLI DEPENDENCIESLERI SYNC ETMEN LAZIM
            admobNativeAdAdapter = AdmobNativeAdAdapter.Builder.with(
                getString(R.string.ad_native_id),
                answerAdapter,
                "small")
                .adItemInterval(3)
                .build()

            binding.recyclerViewFragmentQuestionAnswerAnswersRecyclerView.adapter = admobNativeAdAdapter
        })

    }














    private fun unixtsToDate(timestamp:String):String{
        // post zamanını gösterme kodu
        val unixTimestamp = timestamp
        val formattedDateTime = getLocalizedDateTime(unixTimestamp)
        var postTime = formattedDateTime.substring(11,16)
        var yyyy = formattedDateTime.substring(0,4)
        var mm = formattedDateTime.substring(5,7)
        var dd = formattedDateTime.substring(8,10)
        var postDate = "$dd/$mm/$yyyy"

        val nowTimeStamp = System.currentTimeMillis().toString()

        val timeDifference = (nowTimeStamp.substring(0, nowTimeStamp.length - 3).toLong() - unixTimestamp.substring(0, unixTimestamp.length - 3).toLong())
        // timedifference saniye cinsinden gelir

        val min = timeDifference/60 // üstünden kaç dakika geçmiş onu gösterir
        val hour = timeDifference/3600 // üstünden kaç saat geçmiş onu gösterir
        //Log.e("times: ", "min: $min hour: $hour")
        var text: String = String()
        if (min >= 0 && min < 60) {
            text = "$min ${getString(R.string.minutes_ago)}"

        } else if (hour >= 1 && hour < 24) {
            text = "$hour ${getString(R.string.hours_ago)}"
        } else {
            text =  postTime + " - " + postDate
        }

        return text
    }

    private fun getLocalizedDateTime(unixTime: String): String {
        // Unix zamanını milisaniye cinsine çevir
        val date = Date(unixTime.toLong() * 1)

        // Cihazın mevcut dil ve bölge ayarlarını al
        val locale = Locale.getDefault()

        // Tarih ve saat formatını belirle
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale)

        // Cihazın zaman dilimini al
        val timeZone = TimeZone.getDefault()
        dateFormat.timeZone = timeZone

        // Tarih ve saati formatla ve döndür
        return dateFormat.format(date)
    }





}