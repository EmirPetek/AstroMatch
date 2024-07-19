package com.emirpetek.mybirthdayreminder.ui.fragment.social

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.provider.MediaStore
import android.text.Layout
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.Question
import com.emirpetek.mybirthdayreminder.databinding.FragmentAskQuestionBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.social.AskQuestionFragmentImageAdapter
import com.emirpetek.mybirthdayreminder.viewmodel.social.AskQuestionViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.UUID

class AskQuestionFragment : Fragment() {

    private val viewModel: AskQuestionViewModel by viewModels()
    private lateinit var binding: FragmentAskQuestionBinding
    private lateinit var selectedImagesAdapter: AskQuestionFragmentImageAdapter
    private var selectedImages = ArrayList<Uri>()
    private var imgUrlRefList = ArrayList<String>()
    private var imagesToUpload = 0
    private var uploadedImages = 0
    private var alertDialog: AlertDialog? = null


    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 1001
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAskQuestionBinding.inflate(inflater,container,false)
        binding.fragmentObj = this

        hideBottomNav()
        bindShareButton()
        binding.imageViewAskQuestionAddPhoto.setOnClickListener { pickImages() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            Log.e("askQuestionFragment","selected images onviewcreated: ")
            binding.recyclerViewAskQuestionSelectedPhotos.setHasFixedSize(true)
            binding.recyclerViewAskQuestionSelectedPhotos.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            selectedImagesAdapter = AskQuestionFragmentImageAdapter(requireContext(),selectedImages)
            binding.recyclerViewAskQuestionSelectedPhotos.adapter = selectedImagesAdapter

    }

    private fun hideBottomNav(){
        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav?.visibility = View.GONE
    }

    fun previousPage(view: View){
        findNavController().popBackStack()
    }
    private fun pickImages() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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
                selectedImagesAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun bindShareButton(){
        binding.buttonAskQuestionFragmentShare.setOnClickListener {
            if (binding.editTextAskQuestionMessage.text.toString().isEmpty()){
                Toast.makeText(requireContext(),getString(R.string.fill_all_place),Toast.LENGTH_SHORT).show()
            }else {
                showLoadingAlert()
                uploadDataToDB()
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

    private fun uploadDataToDB() {
        imagesToUpload = selectedImages.size
        if (imagesToUpload == 0){
            imgUrlRefList.add("null")
            addQuestionPostDataToDatabase()
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
                    imgUrlRefList.add(imgPathString)
                    uploadedImages++
                    if (uploadedImages == imagesToUpload) {
                        // Tüm resimler yüklendiğinde yapılacak işlem
                        addQuestionPostDataToDatabase()
                    }
                    // Resmin URL'sini veritabanına kaydedebilirsin
                   // saveImageUrlToDatabase(uri.toString())
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(requireContext(),getString(R.string.something_wrong),Toast.LENGTH_SHORT).show()
                // Yükleme başarısız olduysa hata işlemlerini burada yapabilirsin
            }
    }

    private fun addQuestionPostDataToDatabase(){
        val question = Question(
            "",
            Firebase.auth.currentUser!!.uid,
            binding.editTextAskQuestionMessage.text.toString(),
            imgUrlRefList,
            System.currentTimeMillis(),
            "0",
            0
        )
        viewModel.insertQuestion(question)
        lifecycleScope.launch {
            viewModel.questionAdded.collect{ isAdded ->
                if (isAdded){
                    Toast.makeText(requireContext(),getString(R.string.post_shared),Toast.LENGTH_SHORT).show()
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
            databaseReference.child("askQuestionPhotos").child(imageId).setValue(imageUrl)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Image URL saved to database", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Failed to save image URL", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }




}