package com.emirpetek.mybirthdayreminder.viewmodel.chats

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emirpetek.mybirthdayreminder.data.entity.chat.Chat
import com.emirpetek.mybirthdayreminder.data.entity.chat.UserChats
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.data.repo.chat.ChatRepo
import com.emirpetek.mybirthdayreminder.data.repo.user.UserRepo

class ChatListViewModel : ViewModel() {
    private val chatRepo = ChatRepo()
    private val userRepo = UserRepo()

    var chatIDList = MutableLiveData<ArrayList<UserChats>>()
    var chatListLiveData = MutableLiveData<ArrayList<Chat>>()
    var userData : MutableLiveData<User>

    init {
        chatIDList = chatRepo.chatIDList
        chatListLiveData = chatRepo.chatListLiveData
        userData = userRepo.getUserAsync()
    }

    fun getChatIDs(userID:String){
        chatRepo.getChatIDs(userID)
    }

    fun getChats(chatList: ArrayList<UserChats>){
        chatRepo.getChatData(chatList)
    }

    fun getUserData(userID: String) {
        userRepo.getUserDataAsync(userID)
    }
}