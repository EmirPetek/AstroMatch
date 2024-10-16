package com.emirpetek.mybirthdayreminder.ui.fragment.chats

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.emirpetek.mybirthdayreminder.R
import com.emirpetek.mybirthdayreminder.data.entity.chat.Chat
import com.emirpetek.mybirthdayreminder.data.entity.chat.UserChats
import com.emirpetek.mybirthdayreminder.databinding.FragmentChatListBinding
import com.emirpetek.mybirthdayreminder.ui.adapter.chats.ChatListFragmentAdapter
import com.emirpetek.mybirthdayreminder.ui.util.bottomNavigation.ManageBottomNavigationVisibility
import com.emirpetek.mybirthdayreminder.viewmodel.chats.ChatListViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ChatListFragment : Fragment() {


    private val viewModel: ChatListViewModel by viewModels()
    private lateinit var binding:FragmentChatListBinding
    private lateinit var adapter:ChatListFragmentAdapter
    private val ownUserID = Firebase.auth.currentUser!!.uid
    var newChatList : List<Chat> = listOf()
    var newChatIDList : ArrayList<UserChats> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatListBinding.inflate(inflater,container,false)
       // Log.e("start: ", System.currentTimeMillis().toString())

        ManageBottomNavigationVisibility(requireActivity()).showBottomNav()

        binding.progressBarChatListChats.visibility = View.VISIBLE

        viewModel.getLikes(ownUserID)
        viewModel.likeList.observe(viewLifecycleOwner, Observer { it ->
            val matchesNumber = it.size
            if (matchesNumber > 99) binding.textViewChatListMatchesNumber.text = "+99"
            else binding.textViewChatListMatchesNumber.text = matchesNumber.toString()
        })

        binding.layoutChatListMatches.setOnClickListener { findNavController().navigate(R.id.action_chatListFragment_to_comeLikesFragment) }


        newChatIDList.clear()
        viewModel.getChatIDs(ownUserID)
        viewModel.chatIDList.observe(viewLifecycleOwner, Observer { chatIDList ->
            newChatIDList = chatIDList
            viewModel.getChats(newChatIDList)
            viewModel.chatListLiveData.observe(viewLifecycleOwner, Observer { chatList ->
                newChatList = listOf()
                newChatList = chatList.sortedByDescending { it.lastMessageTimestamp }
                //Log.e("BOYUTU ", newChatList.size.toString())

                binding.recyclerViewChatList.setHasFixedSize(true)
                binding.recyclerViewChatList.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                adapter = ChatListFragmentAdapter(requireContext(),newChatList,viewModel,viewLifecycleOwner,binding.progressBarChatListChats)
                binding.recyclerViewChatList.adapter = adapter

                if (newChatList.isEmpty()){
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