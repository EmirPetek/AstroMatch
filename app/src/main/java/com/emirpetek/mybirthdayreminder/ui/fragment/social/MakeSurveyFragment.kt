package com.emirpetek.mybirthdayreminder.ui.fragment.social

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.Survey
import com.emirpetek.mybirthdayreminder.databinding.FragmentMakeSurveyBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.social.MakeSurveyFragmentOptionsAdapter
import com.emirpetek.mybirthdayreminder.ui.adapter.social.MakeSurveyFragmentImageAdapter
import com.emirpetek.mybirthdayreminder.viewmodel.social.MakeSurveyViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import java.util.UUID

class MakeSurveyFragment : Fragment() {

    private val viewModel: MakeSurveyViewModel by viewModels()
    private lateinit var binding: FragmentMakeSurveyBinding
    private lateinit var selectedImagesAdapter: MakeSurveyFragmentImageAdapter
    private lateinit var optionsAdapter: MakeSurveyFragmentOptionsAdapter
    private var selectedImages = ArrayList<Uri>()
    private var options = ArrayList<String>()
    private var alertDialog: AlertDialog? = null
    private var imgUrlRefList = ArrayList<String>()
    private var imagesToUpload = 0
    private var uploadedImages = 0


    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMakeSurveyBinding.inflate(inflater, container, false)
        binding.fragmentObjMakeSurvey = this
        hideBottomNav()

        addOptionRV()

        binding.imageViewMakeSurveyAddPhoto.setOnClickListener { pickImages() }
        bindShareButton()
        return binding.root
    }

    private fun hideBottomNav() {
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav?.visibility = View.GONE
    }

    fun previousPage(view: View) {
        findNavController().popBackStack()
    }

    private fun addOptionRV(){
        binding.buttonMakeSurveyAddOption.setOnClickListener {
            val optionText = binding.editTextMakeSurveyOptionEdit.text.toString()
            if (optionText.isNotEmpty()){
                options.add(optionText)
                binding.recyclerViewMakeSurveyOptions.setHasFixedSize(true)
                binding.recyclerViewMakeSurveyOptions.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL  , false)
                optionsAdapter = MakeSurveyFragmentOptionsAdapter(requireContext(), options)
                binding.recyclerViewMakeSurveyOptions.adapter = optionsAdapter
                binding.editTextMakeSurveyOptionEdit.text.clear()
            }else{
                showToast(requireContext().getString(R.string.fill_option_place))
            }
        }



    }

    private fun showToast(message:String){
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }

    private fun bindShareButton(){
        binding.buttonMakeSurveyFragmentShare.setOnClickListener {
            if (binding.editTextMakeSurveyMessage.text.toString().isEmpty()){
                showToast(getString(R.string.fill_all_place))
            }else if (options.size < 2){
                showToast(getString(R.string.options_at_least_two_item))
            } else {
                showLoadingAlert()
                uploadDataToDatabase()
            }
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

    private fun uploadDataToDatabase(){
        imagesToUpload = selectedImages.size
        if (imagesToUpload == 0){
            imgUrlRefList.add("null")
            addSurveyPostDataToDatabase()
        }else {
            uploadedImages = 0
            for (uri in selectedImages) {
                uploadImageToDatabase(uri)
            }
        }
    }



    private fun pickImages() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewMakeSurveySelectedPhotos.setHasFixedSize(true)
        binding.recyclerViewMakeSurveySelectedPhotos.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        selectedImagesAdapter = MakeSurveyFragmentImageAdapter(requireContext(), selectedImages)
        binding.recyclerViewMakeSurveySelectedPhotos.adapter = selectedImagesAdapter

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AskQuestionFragment.REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
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
                selectedImagesAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun uploadImageToDatabase(imageUri: Uri) {
        val storageReference = FirebaseStorage.getInstance().reference
        val imgPathString = "posts/makeSurveyPhoto/${UUID.randomUUID()}.jpg"
        val imageReference = storageReference.child(imgPathString)

        imageReference.putFile(imageUri)
            .addOnSuccessListener {
                imageReference.downloadUrl.addOnSuccessListener { uri ->
                    imgUrlRefList.add(imgPathString)
                    uploadedImages++
                    if (uploadedImages == imagesToUpload) {
                        // Tüm resimler yüklendiğinde yapılacak işlem
                        addSurveyPostDataToDatabase()
                    }
                    // Resmin URL'sini veritabanına kaydedebilirsin
                    // saveImageUrlToDatabase(uri.toString())
                }
            }
            .addOnFailureListener { exception ->
                // Yükleme başarısız olduysa hata işlemlerini burada yapabilirsin
            }
    }

    private fun addSurveyPostDataToDatabase(){
        val survey = Survey(
            "",
            Firebase.auth.currentUser!!.uid,
            binding.editTextMakeSurveyMessage.text.toString(),
            options,
            imgUrlRefList,
            System.currentTimeMillis(),
            "0",
            0
        )


        viewModel.insertSurvey(survey)
        lifecycleScope.launch {
            viewModel.surveyAdded.collect{ isAdded ->
                if (isAdded){
                    showToast(getString(R.string.post_shared))
                    closeLoadingAlert()
                    findNavController().popBackStack()

                }
            }
        }
    }

    private fun saveImageUrlToDatabase(imageUrl: String) {
        // burası veritabanına kaydetme kısmı.
        val databaseReference = FirebaseDatabase.getInstance().reference
        val imageId = databaseReference.push().key

        if (imageId != null) {
            databaseReference.child("makeSurveyPhotos").child(imageId).setValue(imageUrl)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Image URL saved to database",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to save image URL",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}