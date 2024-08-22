package com.emirpetek.mybirthdayreminder.ui.fragment.chats

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.emirpetek.mybirthdayreminder.databinding.FragmentChatListBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.chats.ChatListFragmentAdapter
import com.emirpetek.mybirthdayreminder.viewmodel.chats.ChatListViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChatListFragment : Fragment() {


    private val viewModel: ChatListViewModel by viewModels()
    private lateinit var binding:FragmentChatListBinding
    private lateinit var adapter:ChatListFragmentAdapter
    private val ownUserID = Firebase.auth.currentUser!!.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatListBinding.inflate(inflater,container,false)
       // Log.e("start: ", System.currentTimeMillis().toString())

        binding.progressBarChatListChats.visibility = View.VISIBLE

        viewModel.getChatIDs(ownUserID)
        viewModel.chatIDList.observe(viewLifecycleOwner, Observer { chatIDList ->
            viewModel.getChats(chatIDList)
            viewModel.chatListLiveData.observe(viewLifecycleOwner, Observer { chatList ->
                val newChatList = chatList.sortedByDescending { it.lastMessageTimestamp }
                binding.recyclerViewChatList.setHasFixedSize(true)
                binding.recyclerViewChatList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                adapter = ChatListFragmentAdapter(requireContext(),newChatList,viewModel,viewLifecycleOwner,binding.progressBarChatListChats)
                binding.recyclerViewChatList.adapter = adapter

                if (chatList.isNullOrEmpty()){
                    binding.textViewChatListFragmentNoChatHere.visibility = View.VISIBLE
                    binding.progressBarChatListChats.visibility = View.GONE
                }else{
                    binding.textViewChatListFragmentNoChatHere.visibility = View.GONE
                    binding.progressBarChatListChats.visibility = View.GONE
                }

            })
        })



        return binding.root
    }
}