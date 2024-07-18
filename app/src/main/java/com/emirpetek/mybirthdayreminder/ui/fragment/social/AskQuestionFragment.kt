package com.emirpetek.mybirthdayreminder.ui.fragment.social

import android.app.Activity
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentAskQuestionBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.social.AskQuestionFragmentImageAdapter
import com.emirpetek.mybirthdayreminder.viewmodel.social.AskQuestionViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class AskQuestionFragment : Fragment() {

    private val viewModel: AskQuestionViewModel by viewModels()
    private lateinit var binding: FragmentAskQuestionBinding
    private lateinit var selectedImagesAdapter: AskQuestionFragmentImageAdapter
    private var selectedImages = ArrayList<Uri>()

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

    private fun uploadImageToDatabase(imageUri: Uri) {
        val storageReference = FirebaseStorage.getInstance().reference
        val imageReference = storageReference.child("askQuestionPhotos/${UUID.randomUUID()}.jpg")

        imageReference.putFile(imageUri)
            .addOnSuccessListener {
                imageReference.downloadUrl.addOnSuccessListener { uri ->
                    // Resmin URL'sini veritabanına kaydedebilirsin
                   // saveImageUrlToDatabase(uri.toString())
                }
            }
            .addOnFailureListener { exception ->
                // Yükleme başarısız olduysa hata işlemlerini burada yapabilirsin
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