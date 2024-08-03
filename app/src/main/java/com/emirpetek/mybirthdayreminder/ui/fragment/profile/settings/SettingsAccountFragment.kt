package com.emirpetek.mybirthdayreminder.ui.fragment.profile.settings

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.databinding.FragmentSettingsAccountBinding
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
            val createDate = unixtsToDate(user.created_at.toString())
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

        //uploadImageToFirebaseStorage(imageUri) <-- databaseye yükler
            // TODO: foto dbye yüklenene kadar alert göster, yüklendikten sonra urisini userdaki img ile yer değiştir ve  userı update et
        }
    }

    private fun uploadImageToFirebaseStorage(imageUri: Uri?) {
        if (imageUri != null) {
            val pathName = "${userID}_${System.currentTimeMillis()}_${UUID.randomUUID()}"
            val storageReference = FirebaseStorage.getInstance().getReference("users/userImages/$pathName.jpg")
            storageReference.putFile(imageUri)
                .addOnSuccessListener { taskSnapshot ->
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        // Yükleme başarılı, downloadUrl ile işlemlere devam edebilirsiniz.
                    }
                }
                .addOnFailureListener { exception ->
                }
        }
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