package com.emirpetek.mybirthdayreminder.ui.fragment.profile.settings

import android.app.Activity.RESULT_OK
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
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.databinding.FragmentSettingsAccountBinding
import com.emirpetek.mybirthdayreminder.ui.util.calculateTime.CalculateShareTime
import com.emirpetek.mybirthdayreminder.viewmodel.profile.SettingsAccountViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

class SettingsAccountFragment : Fragment() {

    private val viewModel: SettingsAccountViewModel by viewModels()
    private lateinit var binding: FragmentSettingsAccountBinding
    lateinit var user: User
    val userID = Firebase.auth.currentUser?.uid
    val PICK_IMAGE_REQUEST = 1
    private var alertDialog: AlertDialog? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsAccountBinding.inflate(inflater,container,false)
        binding.classObject = this

      //  binding.imageViewSettingsAccountFragmentBackButton.setOnClickListener {  }

        viewModel.getUserData(userID!!)
        viewModel.userData.observe(viewLifecycleOwner, Observer { it ->
            user = it
            val createDate = CalculateShareTime(requireContext()).unixtsToDate(user.created_at.toString())
            binding.editTextSettingsAccountJoinedAt.setText(createDate)
            bindUserImage(user.profile_img)
            binding.userObject = this
        })

        return binding.root
    }

    fun goBack(){
        findNavController().popBackStack()
    }

    fun bindUserImage(uri: String){
        val imgUri : Any
        if (uri.equals("no_photo")){
            imgUri = R.drawable.baseline_person_24
        }else{
            imgUri = uri
        }
        Glide.with(requireContext())
            .load(imgUri)
            .into(binding.imageViewSettingsAccountPhoto)
    }

    fun openFileChooser() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val imageUri: Uri? = data.data
            Glide.with(requireContext())
                .load(imageUri)
                .circleCrop()
                .into(binding.imageViewSettingsAccountPhoto)

            binding.textViewSettingsAccountUpdateData.setOnClickListener {
                uploadImageToFirebaseStorage(imageUri)
            }

        //uploadImageToFirebaseStorage(imageUri) <-- databaseye yükler
        }
    }

    private fun uploadImageToFirebaseStorage(imageUri: Uri?) {
        showLoadingAlert()
        if (imageUri != null) {
            val pathName = "${userID}_${System.currentTimeMillis()}_${UUID.randomUUID()}"
            val storageReference = FirebaseStorage.getInstance().getReference("users/profileImages/$pathName.jpg")
            storageReference.putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        user.profile_img = downloadUrl
                        updateUserData()
                    }
                }
                .addOnFailureListener { exception ->
                }
        }
    }

    private fun updateUserData(){
        viewModel.updateUserData(user)
        Toast.makeText(requireContext(),getString(R.string.datas_updated),Toast.LENGTH_SHORT).show()
        closeLoadingAlert()
        goBack()
    }


    private fun showLoadingAlert() {
        if (alertDialog == null) {
            val dialogView = layoutInflater.inflate(R.layout.alert_wait_screen, null)
            dialogView.findViewById<TextView>(R.id.textViewAlertWaitScreenPleaseWait).setText(getString(R.string.data_updating))
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


}