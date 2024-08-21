package com.emirpetek.mybirthdayreminder.ui.fragment.messages

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.emirpetek.mybirthdayreminder.data.entity.chat.Message
import com.emirpetek.mybirthdayreminder.data.entity.chat.MessageType
import com.emirpetek.mybirthdayreminder.databinding.FragmentMessagesBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.messages.MessagesFragmentAdapter
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility
import com.emirpetek.mybirthdayreminder.viewmodel.messages.MessagesViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MessagesFragment : Fragment() {

    private val viewModel: MessagesViewModel by viewModels()
    private lateinit var binding:FragmentMessagesBinding
    private lateinit var messageAdapter:MessagesFragmentAdapter
    private var messageList = ArrayList<Message>()
    private val ownUserID = Firebase.auth.currentUser!!.uid

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

        binding.imageViewMessagesFragmentBackButton.setOnClickListener { findNavController().popBackStack() }

        binding.progressBarMessagesFragmentLoadMessages.visibility = View.VISIBLE
        viewModel.startChat(ownUserID,anotherUserID)
        viewModel.currentChatID.observe(viewLifecycleOwner, Observer { chatID ->

            binding.buttonMessagesFragmentSendMessage.setOnClickListener { sendMessage(chatID!!,binding.editTextMessagesFragmentMessage.text.toString()) }

                viewModel.getMessages(chatID!!)
                viewModel.messages.observe(viewLifecycleOwner, Observer { messages ->

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

    private fun sendMessage(chatID: String, msg:String){
        val type = MessageType.TEXT // eğer resim seçilmişse image, yazı yazılmışsa text olacak, mixleme ileride düşünülebilir
        viewModel.sendMessage(chatID,msg, type){ success ->
            if (success){
                binding.editTextMessagesFragmentMessage.text.clear()
            }else{
                Toast.makeText(requireContext(),"mesaj gönderilmeedi", Toast.LENGTH_SHORT).show()
            }
        }
    }
}