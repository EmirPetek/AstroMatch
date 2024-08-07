package com.emirpetek.mybirthdayreminder.ui.fragment.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.UserGalleryPhoto
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.databinding.FragmentShareProfileGalleryPhotosBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.profile.ProfileFragmentProfileGalleryPhotosAdapter
import com.emirpetek.mybirthdayreminder.ui.adapter.profile.ShareProfileGalleryPhotosFragmentAdapter
import com.emirpetek.mybirthdayreminder.ui.fragment.social.sharePost.AskQuestionFragment.Companion.REQUEST_CODE_PICK_IMAGE
import com.emirpetek.mybirthdayreminder.viewmodel.profile.ShareProfileGalleryPhotosViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import java.util.UUID

class ShareProfileGalleryPhotosFragment : Fragment() {

    private val viewModel: ShareProfileGalleryPhotosViewModel by viewModels()
    private lateinit var binding: FragmentShareProfileGalleryPhotosBinding
    private lateinit var adapter: ShareProfileGalleryPhotosFragmentAdapter
    private var alertDialog: AlertDialog? = null
    private var selectedImages = ArrayList<Uri>()
    private var imgUrlRefList = ArrayList<UserGalleryPhoto>()
    private var imagesToUpload = 0
    private var uploadedImages = 0
    private var mInterstitialAd: InterstitialAd? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShareProfileGalleryPhotosBinding.inflate(inflater,container,false)

        hideBottomNav()
        loadAds()
        pickImages()
        bindShareButton()

        binding.buttonProfileGalleryCancel.setOnClickListener { findNavController().popBackStack() }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.recyclerViewFragmentShareProfileGalleryPhotos.setHasFixedSize(true)
        binding.recyclerViewFragmentShareProfileGalleryPhotos.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL,false)
        adapter = ShareProfileGalleryPhotosFragmentAdapter(requireContext(),selectedImages)



        val snapHelper: SnapHelper = LinearSnapHelper()
        binding.recyclerViewFragmentShareProfileGalleryPhotos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val snapView = snapHelper.findSnapView(layoutManager)
                    val snapPosition = layoutManager.getPosition(snapView!!)

                    // Pozisyonu güncelle
                    val adapterPosition = snapPosition + 1
                    val listSize = selectedImages.size
                    val positionText = "$adapterPosition/$listSize"
                    binding.textViewShowPhotosPhotoOrder.text = positionText
                }
            }
        })

        val pos = "1/${selectedImages.size}"
        binding.textViewShowPhotosPhotoOrder.text = pos

        snapHelper.attachToRecyclerView(binding.recyclerViewFragmentShareProfileGalleryPhotos)

        binding.recyclerViewFragmentShareProfileGalleryPhotos.adapter = adapter


    }

    private fun hideBottomNav(){
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav?.visibility = View.GONE
    }

    private fun loadAds(){
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(requireContext(),getString(R.string.ad_interstitial_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
            }
        })
    }

    private fun bindShareButton(){
        binding.buttonProfileGalleryShare.setOnClickListener {
                showLoadingAlert()
                uploadDataToDB()
        }
    }

    fun showAd(){
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(requireActivity())
        } else {
            Log.e("askquestionfragment", "reklam gösterilirken hata oluştu")
        }
    }

    private fun showLoadingAlert() {
        if (alertDialog == null) {
            val dialogView = layoutInflater.inflate(R.layout.alert_wait_screen, null)
            val alertDialogBuilder = AlertDialog.Builder(requireContext())
            alertDialogBuilder.setView(dialogView)
            alertDialog = alertDialogBuilder.create().apply {
                setCancelable(false)
                setCanceledOnTouchOutside(false)
            }
        }
        alertDialog?.show()
    }

    private fun closeLoadingAlert() {
        alertDialog?.let {
            it.dismiss()
            alertDialog = null
        }
    }

    fun pickImages() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

       // eğer resim seçilmeden geri dönülürse profil ekranına geri dön
        if (selectedImages.size == 0) findNavController().popBackStack()

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            data?.let { intentData ->
                val clipData = intentData.clipData
                if (clipData != null) {
                    // Birden fazla resim seçildi
                    for (i in 0 until clipData.itemCount) {
                        val uri = clipData.getItemAt(i).uri
                        selectedImages.add(uri)
                    }
                } else {
                    // Tek bir resim seçildi
                    val uri = intentData.data
                    uri?.let {
                        selectedImages.add(it)
                    }
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun uploadDataToDB() {
        imagesToUpload = selectedImages.size
        if (imagesToUpload == 0){
            val obj = UserGalleryPhoto("null",0)
            imgUrlRefList.add(obj)
            //addQuestionPostDataToDatabase()
        }else {
            uploadedImages = 0
            for (uri in selectedImages) {
                uploadImageToDatabase(uri)
            }
        }
    }

    private fun uploadImageToDatabase(imageUri: Uri) {
        val storageReference = FirebaseStorage.getInstance().reference
        var imgPathString = "posts/askQuestionPhoto/${UUID.randomUUID()}.jpg"
        val imageReference = storageReference.child(imgPathString)

        imageReference.putFile(imageUri)
            .addOnSuccessListener {
                imageReference.downloadUrl.addOnSuccessListener { uri ->
                    val obj = UserGalleryPhoto(uri.toString(),System.currentTimeMillis())
                    imgUrlRefList.add(obj)
                    uploadedImages++
                    if (uploadedImages == imagesToUpload) {

                        // Tüm resimler yüklendiğinde yapılacak işlem
                        addImageUrlToDatabase()
                    }
                    // Resmin URL'sini veritabanına kaydedebilirsin
                    // saveImageUrlToDatabase(uri.toString())
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(),getString(R.string.something_wrong), Toast.LENGTH_SHORT).show()
                // Yükleme başarısız olduysa hata işlemlerini burada yapabilirsin
            }
    }

    private fun addImageUrlToDatabase(){
        viewModel.insertProfileGalleryURLs(Firebase.auth.currentUser!!.uid,imgUrlRefList)
        lifecycleScope.launch {
            viewModel.photosDataAdded.collect{ isAdded ->
                if (isAdded){
                    Toast.makeText(requireContext(),getString(R.string.post_shared),Toast.LENGTH_SHORT).show()
                    closeLoadingAlert()
                    showAd()
                    findNavController().popBackStack()

                }
            }
        }
    }
}