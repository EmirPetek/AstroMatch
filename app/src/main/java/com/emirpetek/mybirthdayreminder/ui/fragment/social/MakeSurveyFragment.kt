package com.emirpetek.mybirthdayreminder.ui.fragment.social

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.databinding.FragmentMakeSurveyBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.social.MakeSurveyFragmentOptionsAdapter
import com.emirpetek.mybirthdayreminder.ui.adapter.social.MakeSurveyFragmentImageAdapter
import com.emirpetek.mybirthdayreminder.viewmodel.social.MakeSurveyViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class MakeSurveyFragment : Fragment() {

    private val viewModel: MakeSurveyViewModel by viewModels()
    private lateinit var binding: FragmentMakeSurveyBinding
    private lateinit var selectedImagesAdapter: MakeSurveyFragmentImageAdapter
    private lateinit var optionsAdapter: MakeSurveyFragmentOptionsAdapter
    private var selectedImages = ArrayList<Uri>()
    private var options = ArrayList<String>()

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
        val imageReference = storageReference.child("makeSurveyPhotos/${UUID.randomUUID()}.jpg")

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