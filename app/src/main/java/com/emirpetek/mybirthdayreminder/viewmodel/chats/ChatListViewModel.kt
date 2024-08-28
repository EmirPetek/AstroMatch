package com.emirpetek.mybirthdayreminder.viewmodel.chats

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emirpetek.mybirthdayreminder.data.entity.chat.Chat
import com.emirpetek.mybirthdayreminder.data.entity.chat.UserChats
import com.emirpetek.mybirthdayreminder.data.entity.like.Like
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.data.repo.chat.ChatRepo
import com.emirpetek.mybirthdayreminder.data.repo.like.LikeRepo
import com.emirpetek.mybirthdayreminder.data.repo.user.UserRepo
import kotlinx.coroutines.launch

class ChatListViewModel : ViewModel() {
    private val chatRepo = ChatRepo()
    private val userRepo = UserRepo()
    private val likeRepo = LikeRepo()

    var chatIDList = MutableLiveData<ArrayList<UserChats>>()
    var chatListLiveData = MutableLiveData<ArrayList<Chat>>()
    var userData : MutableLiveData<User>
    var likeList = MutableLiveData<List<Like>>()

    init {
        chatIDList = chatRepo.chatIDList
        chatListLiveData = chatRepo.chatListLiveData
        userData = userRepo.getUserAsync()
        likeList = likeRepo.getLikeList()

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

    fun getLikes(userID: String){
        viewModelScope.launch {
            likeRepo.getLikes(userID)
        }
    }
}