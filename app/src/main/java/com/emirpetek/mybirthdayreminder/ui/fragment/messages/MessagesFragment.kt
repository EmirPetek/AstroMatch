package com.emirpetek.mybirthdayreminder.ui.fragment.messages

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.PrecomputedText.Params
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar.LayoutParams
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.marginBottom
import androidx.core.view.marginTop
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.chat.Message
import com.emirpetek.mybirthdayreminder.data.entity.chat.MessageType
import com.emirpetek.mybirthdayreminder.databinding.FragmentMessagesBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.messages.MessagesFragmentAdapter
import com.emirpetek.mybirthdayreminder.ui.adapter.social.sharePost.question.AskQuestionFragmentImageAdapter
import com.emirpetek.mybirthdayreminder.ui.fragment.social.sharePost.AskQuestionFragment.Companion.REQUEST_CODE_PICK_IMAGE
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility
import com.emirpetek.mybirthdayreminder.viewmodel.messages.MessagesViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class MessagesFragment : Fragment() {

    private val viewModel: MessagesViewModel by viewModels()
    private lateinit var binding:FragmentMessagesBinding
    private lateinit var messageAdapter:MessagesFragmentAdapter
    private var messageList = ArrayList<Message>()
    private val ownUserID = Firebase.auth.currentUser!!.uid

    private var selectedImages = ArrayList<Uri>()
    private lateinit var selectedImagesAdapter: AskQuestionFragmentImageAdapter
    private var imagesToUpload = 0
    private var uploadedImages = 0
    private var imgUrlRefList = ArrayList<String>()
    private var alertDialog: AlertDialog? = null


    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 1001
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessagesBinding.inflate(inflater,container,false)

        ManageBottomNavigationVisibility(requireActivity()).hideBottomNav()

        val anotherUserID: String = arguments?.getString("anotherUserID")!!

        viewModel.getAnotherUserData(anotherUserID).observe(viewLifecycleOwner, Observer { anotherUserData ->
            binding.textViewMessagesFragmentToolbarTitle.text = anotherUserData.fullname
            Glide.with(this).load(anotherUserData.profile_img).circleCrop().into(binding.imageViewMessagesFragmentToolbarUserPhoto)
        })



        binding.layoutMessagePersonDetailToolbar.setOnClickListener {
            findNavController()
                .navigate(R.id.action_messagesFragment_to_profileFragment
                    ,Bundle().apply { putString("userID",anotherUserID) }) }

        binding.imageViewMessagesFragmentBackButton.setOnClickListener { findNavController().popBackStack() }

        binding.progressBarMessagesFragmentLoadMessages.visibility = View.VISIBLE
        viewModel.startChat(ownUserID,anotherUserID)
        viewModel.currentChatID.observe(viewLifecycleOwner, Observer { chatID ->

            viewModel.isChatBefore.observe(viewLifecycleOwner, Observer { isChatBefore ->
                if (!isChatBefore) sendMessage(chatID!!,MessageType.TEXT,"Hello!")
            })

            binding.buttonMessagesFragmentSendMessage.setOnClickListener {
                if (binding.editTextMessagesFragmentMessage.text.toString().isEmpty()){
                    Toast.makeText(requireContext(),getString(R.string.no_send_empty_text),Toast.LENGTH_SHORT).show()
                }else checkAndSendMessage(chatID!!)
            }

            binding.buttonMessagesFragmentMedia.setOnClickListener { checkGalleryPermission() }

                viewModel.getMessages(chatID!!)
                viewModel.messages.observe(viewLifecycleOwner, Observer { messages ->


                    viewModel.markMessagesAsRead(chatID)
                    if (messages.isNullOrEmpty()) binding.textViewMessagesFragmentNoMessageText.visibility = View.VISIBLE

                    binding.recyclerViewMessageFragmentMessages.setHasFixedSize(true)
                    binding.recyclerViewMessageFragmentMessages.layoutManager = LinearLayoutManager(requireContext())
                    viewModel.getAnotherUserData(anotherUserID).observe(viewLifecycleOwner, Observer {  anotherUserData ->

                        messageAdapter = MessagesFragmentAdapter(requireContext(),messages,anotherUserData.profile_img)
                        binding.recyclerViewMessageFragmentMessages.adapter = messageAdapter
                        binding.recyclerViewMessageFragmentMessages.scrollToPosition(messages.size - 1)
                        binding.progressBarMessagesFragmentLoadMessages.visibility = View.GONE

                    })
                })

        })


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewMessagesMedia.setHasFixedSize(true)
        binding.recyclerViewMessagesMedia.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        selectedImagesAdapter = AskQuestionFragmentImageAdapter(requireContext(),selectedImages)
        binding.recyclerViewMessagesMedia.adapter = selectedImagesAdapter
    }

    private fun sendMessage(chatID: String, messageType: MessageType, msg:Any){
            val type = MessageType.TEXT // eğer resim seçilmişse image, yazı yazılmışsa text olacak, mixleme ileride düşünülebilir

            viewModel.sendMessage(chatID,msg, messageType){ success ->
                if (success){
                    binding.editTextMessagesFragmentMessage.text.clear()
                }else{
                    Toast.makeText(requireContext(),"Mesaj Gönderilemedi!", Toast.LENGTH_SHORT).show()
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkGalleryPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            // Kamera izni verilmemiş, izin iste
            Log.e("izin durumu:", "izin yok")
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_MEDIA_IMAGES), REQUEST_CODE_PICK_IMAGE)
        } else {
            Log.e("izin durumu:", "izin var")
            // İzin verilmiş, kamerayı başlat
            pickImages()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data == null || (data.clipData == null && data.data == null)) { // resim seçilmeden geri gelindi
            binding.layoutMessageMedia.visibility = View.GONE
        }else{
            binding.layoutMessageText.visibility = View.GONE
            binding.layoutMessageMedia.visibility = View.VISIBLE
            binding.layoutMessageView.layoutParams.height = -120
        }

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

    private fun checkAndSendMessage(chatID: String) {
        imagesToUpload = selectedImages.size
        if (imagesToUpload == 0){
            imgUrlRefList.add("null")
            sendMessage(chatID!!, MessageType.TEXT,binding.editTextMessagesFragmentMessage.text.toString().trim())
        }else {
            showLoadingAlert()
            uploadedImages = 0
            for (uri in selectedImages) {
                uploadImageToDatabase(uri,chatID)
            }
        }
    }
    private fun uploadImageToDatabase(imageUri: Uri,chatID: String) {
        val storageReference = FirebaseStorage.getInstance().reference
        var imgPathString = "chat/messages/$chatID/${UUID.randomUUID()}.jpg"
        val imageReference = storageReference.child(imgPathString)

        imageReference.putFile(imageUri)
            .addOnSuccessListener {
                imageReference.downloadUrl.addOnSuccessListener { uri ->
                    imgUrlRefList.add(uri.toString())
                    uploadedImages++
                    if (uploadedImages == imagesToUpload) {
                        // Tüm resimler yüklendiğinde yapılacak işlem
                        binding.layoutMessageMedia.visibility = View.GONE
                        binding.layoutMessageText.visibility = View.VISIBLE
                        closeLoadingAlert()
                        Toast.makeText(requireContext(),getString(R.string.photo_message_sent),Toast.LENGTH_SHORT).show()
                        sendMessage(chatID!!, MessageType.IMAGE,imgUrlRefList)
                        imgUrlRefList.clear()
                        selectedImages.clear()
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

    private fun showLoadingAlert() {
        if (alertDialog == null) {
            val dialogView = layoutInflater.inflate(R.layout.alert_wait_screen, null)
            dialogView.findViewById<TextView>(R.id.textViewAlertWaitScreenPleaseWait).text = getString(R.string.photo_message_send_waiting)
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