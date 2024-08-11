package com.emirpetek.mybirthdayreminder.ui.fragment.profile

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.user.UserGalleryPhoto
import com.emirpetek.mybirthdayreminder.data.entity.question.Post
import com.emirpetek.mybirthdayreminder.databinding.FragmentProfileBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.profile.ProfileFragmentPostAdapter
import com.emirpetek.mybirthdayreminder.ui.adapter.profile.userGalleryPhotos.ProfileFragmentProfileGalleryPhotosAdapter
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility
import com.emirpetek.mybirthdayreminder.ui.util.zodiacAndAscendant.CalculateCompatibility
import com.emirpetek.mybirthdayreminder.ui.util.zodiacAndAscendant.GetZodiacAscendant
import com.emirpetek.mybirthdayreminder.viewmodel.profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.rvadapter.AdmobNativeAdAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private val viewModelForCompliance: ProfileViewModel by viewModels()
    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var postAdapter: ProfileFragmentPostAdapter
    private lateinit var galleryPhotoAdapter: ProfileFragmentProfileGalleryPhotosAdapter
    var postList = ArrayList<Post>()
    var userID : String? = null
    var galleryPhotos : ArrayList<UserGalleryPhoto>? = null
    var userType: String = ""
    var isBindComplianceRate = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater,container,false)

        auth = Firebase.auth

        userID = arguments?.getString("userID")
        if(!userID.isNullOrEmpty() && userID != auth.currentUser?.uid){ // user is anyUser

            bindAnyUser()
            userType = "anyUser"
        }else { // own user
            bindOwnUser()
            userType = "ownUser"
        }
        return binding.root
    }

    fun bindAnyUser(){
        ManageBottomNavigationVisibility(requireActivity()).hideBottomNav()
        toolbarAnyUser()
        binding.constraintLayoutFragmentProfilePostLayout.visibility = View.GONE
        //bindComplianceRate()
        bindUserData(userID.toString())
    }

    fun bindOwnUser(){
        ManageBottomNavigationVisibility(requireActivity()).showBottomNav()
        binding.linearLayoutHoroscopeCompatibility.visibility = View.GONE
        toolbarOwnUser()
        bindUserData(auth.currentUser!!.uid)
        bindUserPost()
    }

    private fun toolbarAnyUser(){
        Glide.with(this)
            .load(R.drawable.baseline_arrow_back_ios_24)
            .into(binding.imageViewFragmentProfileToolbar)
        binding.imageViewFragmentProfileToolbar.setOnClickListener { findNavController().popBackStack() }
        binding.imageViewProfileFragmentSettings.visibility = View.GONE

    }

    private fun toolbarOwnUser(){
        Glide.with(this)
            .load(R.drawable.baseline_person_24)
            .into(binding.imageViewFragmentProfileToolbar)
        binding.imageViewProfileFragmentSettings.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_profileSettingsFragment) }
        binding.imageViewProfileFragmentSettings.visibility = View.VISIBLE
    }

    private fun bindZodiacAscendantImage(zodiac:String,ascendant:String){
        val zodiacDrawableResId = when (zodiac.lowercase()) {
            getString(R.string.capricorn).lowercase() -> R.drawable.capricorn
            getString(R.string.aquarius).lowercase() -> R.drawable.aquarius
            getString(R.string.pisces).lowercase() -> R.drawable.pisces
            getString(R.string.aries).lowercase() -> R.drawable.aries
            getString(R.string.taurus).lowercase() -> R.drawable.taurus
            getString(R.string.gemini).lowercase() -> R.drawable.gemini
            getString(R.string.cancer).lowercase() -> R.drawable.cancer
            getString(R.string.leo).lowercase() -> R.drawable.leo
            getString(R.string.virgo).lowercase() -> R.drawable.virgo
            getString(R.string.libra).lowercase() -> R.drawable.libra
            getString(R.string.scorpio).lowercase() -> R.drawable.scorpio
            getString(R.string.sagittarius).lowercase() -> R.drawable.sagittarius
            else -> R.drawable.baseline_error_24 // Varsayılan resim
        }

        val ascendantDrawableResId = when (ascendant.lowercase()) {
            getString(R.string.capricorn).lowercase() -> R.drawable.capricorn
            getString(R.string.aquarius).lowercase() -> R.drawable.aquarius
            getString(R.string.pisces).lowercase() -> R.drawable.pisces
            getString(R.string.aries).lowercase() -> R.drawable.aries
            getString(R.string.taurus).lowercase() -> R.drawable.taurus
            getString(R.string.gemini).lowercase() -> R.drawable.gemini
            getString(R.string.cancer).lowercase() -> R.drawable.cancer
            getString(R.string.leo).lowercase() -> R.drawable.leo
            getString(R.string.virgo).lowercase() -> R.drawable.virgo
            getString(R.string.libra).lowercase() -> R.drawable.libra
            getString(R.string.scorpio).lowercase() -> R.drawable.scorpio
            getString(R.string.sagittarius).lowercase() -> R.drawable.sagittarius
            else -> R.drawable.baseline_error_24 // Varsayılan resim
        }

        /*Glide.with(this)
            .load(zodiacDrawableResId)
            .into(binding.imageViewProfileZodiac)

        Glide.with(this)
            .load(ascendantDrawableResId)
            .into(binding.imageViewProfileAscendant)*/
    }

    fun formatTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

    fun calculateAge(birthDateString: String): Int {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val birthDate: Date = dateFormat.parse(birthDateString) ?: return -1
        val birthCalendar = Calendar.getInstance().apply { time = birthDate }
        val today = Calendar.getInstance()

        var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)

        if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--
        }

        return age
    }



    fun bindUserData(userID:String){
        viewModel.getUser(userID)
        viewModel.user.observe(viewLifecycleOwner, Observer { it ->
            val joined_at = context?.getString(R.string.joined_at)  + " " + formatTimestampToDate(it.created_at)
            val fullname = it.fullname
            val birthdate = it.birthdate
            val zodiac = it.zodiac
            val ascendant = it.ascendant
            val photoUri = it.profile_img
            val biography = it.biography
            var loadUri = ""
            galleryPhotos = it.profileGalleryPhotos //as ArrayList<String>

            bindUserGalleryPhotos()



            loadUri = if(photoUri.equals("no_photo")){
                "https://www.bio.purdue.edu/lab/deng/images/photo_not_yet_available.jpg"
            }else{
                photoUri
            }

            Glide.with(this)
                .load(loadUri)
                .circleCrop()
                .into(binding.imageViewProfilePhoto)

            binding.imageViewProfilePhoto.setOnClickListener {
                val bundle = Bundle()
                val imgList = ArrayList<String>()
                imgList.add(loadUri)
                bundle.putStringArrayList("imageList",imgList)
                findNavController().navigate(R.id.action_profileFragment_to_showPhotosFragment,bundle)
            }
            val zodiacTextObj = GetZodiacAscendant(requireContext())


            binding.textViewProfileFullname.setText(fullname)
            binding.textViewProfileAge.setText(calculateAge(birthdate).toString())
            binding.textViewProfileZodiac.setText(zodiacTextObj.getZodiacOrAscendantSignByIndex(zodiac))
            binding.textViewProfileAscendant.setText(zodiacTextObj.getZodiacOrAscendantSignByIndex(ascendant))
            binding.textViewProfileBiography.setText(biography)
            //  binding.textViewProfileJoinedAt.setText(joined_at)

            if (userID != Firebase.auth.currentUser!!.uid) {
                bindComplianceRate(zodiac)
            }

           // bindZodiacAscendantImage(zodiac,ascendant)

            viewModel.user.removeObserver{viewLifecycleOwner}
        })


    }

    fun bindUserPost(){

        val ownUserID = Firebase.auth.currentUser?.uid!!

        // bundle ile veri kontrolü yapıp eğer veri varsa post type değişecek
        // ve bottombar visibilitysi değişecek
        binding.progressBarFragmentProfilePost.visibility = View.VISIBLE
        binding.textViewFragmentProfilePostTitle.setText(getString(R.string.your_post))
        viewModel.getQuestions(ownUserID)
        viewModel.questionList.observe(viewLifecycleOwner, Observer { it ->
            postList = it as ArrayList<Post>
            if (postList.isNotEmpty()) binding.textViewFragmentProfileNoPostHere.visibility = View.GONE
            postList.sortByDescending { it.timestamp }
            viewModel.getAllQuestionAnswerNumbers(postList)
            viewModel.answerSizeList.observe(viewLifecycleOwner, Observer { answerSize ->
                var answerSizeList = answerSize as ArrayList
                binding.recyclerViewFragmentProfilePosts.setHasFixedSize(true)
                binding.recyclerViewFragmentProfilePosts.layoutManager = LinearLayoutManager(requireContext(),
                    LinearLayoutManager.VERTICAL,false)
                postAdapter = ProfileFragmentPostAdapter(requireContext(),postList,answerSizeList)

                // NATIVE REKLAM İÇİN GEREKEN KODLAR. GITHUB FORKLADIM. ORADAN BAK. GEREKLI DEPENDENCIESLERI SYNC ETMEN LAZIM
                val admobNativeAdAdapter: AdmobNativeAdAdapter = AdmobNativeAdAdapter.Builder.with(
                    getString(R.string.ad_native_id),
                    postAdapter,
                    "custom"
                )
                    .adItemInterval(10)
                    .build()
                binding.recyclerViewFragmentProfilePosts.adapter  = admobNativeAdAdapter
                binding.progressBarFragmentProfilePost.visibility = View.GONE

                // alttaki kod aynı sayfa içerisinde yeni veri gelirse yenilenmemesini sağlar
                //viewModel.questionList.removeObservers(viewLifecycleOwner)

            })

        })
    }


    fun bindUserGalleryPhotos(){

        if (userType.equals("anyUser")){ // başkasının profiline girilmiş
            binding.textViewProfilePhotosAddPhoto.visibility = View.GONE

        }else{ // kendi profiline girmiş
            binding.textViewProfilePhotosAddPhoto.setOnClickListener { addGalleryPhoto() }

        }


        if (galleryPhotos.isNullOrEmpty() || galleryPhotos!!.size == 0){
            binding.textViewFragmentProfileNoPhotoHere.visibility = View.VISIBLE
            binding.progressBarFragmentProfileGalleryPhoto.visibility = View.GONE
            binding.constraintLayoutFragmentProfilePhotoLayout.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            binding.textViewProfilePhotosViewAll.visibility = View.GONE
        }else{

            binding.textViewFragmentProfilePhotoTitle.setText("${getString(R.string.photos)} (${galleryPhotos!!.size})")

            val bundle = Bundle()
            bundle.putParcelableArrayList("imageListProfileGallery",galleryPhotos)
            binding.textViewProfilePhotosViewAll.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_profileGalleryViewAllFragment,bundle) }


            binding.progressBarFragmentProfileGalleryPhoto.visibility = View.VISIBLE
            binding.textViewFragmentProfileNoPhotoHere.visibility = View.GONE
            binding.recyclerViewFragmentProfilePhotos.setHasFixedSize(true)
            binding.recyclerViewFragmentProfilePhotos.layoutManager = LinearLayoutManager(requireContext(),
                LinearLayoutManager.HORIZONTAL,false)
            galleryPhotoAdapter = ProfileFragmentProfileGalleryPhotosAdapter(requireContext(),
                galleryPhotos!!
            )
            binding.recyclerViewFragmentProfilePhotos.adapter = galleryPhotoAdapter
            binding.progressBarFragmentProfileGalleryPhoto.visibility = View.GONE
        }
    }

    fun addGalleryPhoto(){
        findNavController().navigate(R.id.action_profileFragment_to_shareProfileGalleryPhotosFragment)
        //ShareProfileGalleryPhotosFragment().pickImages()
    }

    fun bindComplianceRate(anotherUserZodiac:Int){
        Log.e("current user id: ", Firebase.auth.currentUser!!.uid)
        viewModelForCompliance.getUserAsync(Firebase.auth.currentUser!!.uid)
        viewModelForCompliance.userAsync.observe(viewLifecycleOwner, Observer { ownUser ->
            val ownUserHorosope = ownUser.zodiac
            val anotherUserHoroscope = anotherUserZodiac

            val zodiacTextObj = GetZodiacAscendant(requireContext())
//            Log.e("ldskflşs", "own $ownUserHorosope ve ano $anotherUserHoroscope")
//            Log.e("erwrwerwe", "${ownUser.toString()}")

            val ownZodiacText = zodiacTextObj.getZodiacOrAscendantSignByIndex(ownUserHorosope)
            val anotherZodiacText = zodiacTextObj.getZodiacOrAscendantSignByIndex(anotherUserHoroscope)
            //Log.e("ldskflşs", "$ownZodiacText ve $anotherZodiacText")

            binding.textViewProfileYourHoroscope.setText(ownZodiacText)
            binding.textViewProfileAnotherHoroscope.setText(anotherZodiacText)

            bindZodiacAscendantImage(ownUserHorosope, anotherUserHoroscope)
            val rate = CalculateCompatibility().calculateCompatibility(ownUserHorosope,anotherUserHoroscope)
            binding.textViewProfileFragmentComplianceRate.text = "% $rate"

            // veriler 1 kez çekildikten sonra daha çekilmez

            })

       // viewModelForCompliance.user2.removeObservers(viewLifecycleOwner)

    }

    private fun bindZodiacAscendantImage(ownUserHorosope: Int, anotherUserHoroscope: Int) {
        val ownUserHorosopeDrawableResId = when (ownUserHorosope) {
            10 -> R.drawable.capricorn
            11 -> R.drawable.aquarius
            12 -> R.drawable.pisces
            1 -> R.drawable.aries
            2 -> R.drawable.taurus
            3 -> R.drawable.gemini
            4 -> R.drawable.cancer
            5 -> R.drawable.leo
            6 -> R.drawable.virgo
            7 -> R.drawable.libra
            8 -> R.drawable.scorpio
            9 -> R.drawable.sagittarius
            else -> R.drawable.baseline_error_24 // Varsayılan resim
        }

        val anotherUserHoroscopeDrawableResId = when (anotherUserHoroscope) {
            10 -> R.drawable.capricorn
            11 -> R.drawable.aquarius
            12 -> R.drawable.pisces
            1 -> R.drawable.aries
            2 -> R.drawable.taurus
            3 -> R.drawable.gemini
            4 -> R.drawable.cancer
            5 -> R.drawable.leo
            6 -> R.drawable.virgo
            7 -> R.drawable.libra
            8 -> R.drawable.scorpio
            9 -> R.drawable.sagittarius
            else -> R.drawable.baseline_error_24 // Varsayılan resim
        }


        Glide.with(this)
            .load(ownUserHorosopeDrawableResId)
            .into(binding.imageViewProfileYourHoroscope)

        Glide.with(this)
            .load(anotherUserHoroscopeDrawableResId)
            .into(binding.imageViewProfileAnotherHoroscope)
    }


    override fun onResume() {
        super.onResume()
        //bindUserPost()

    }
}