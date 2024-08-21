package com.emirpetek.mybirthdayreminder.viewmodel.messages

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emirpetek.mybirthdayreminder.data.entity.chat.Message
import com.emirpetek.mybirthdayreminder.data.entity.chat.MessageType
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.data.repo.chat.ChatRepo
import com.emirpetek.mybirthdayreminder.data.repo.user.UserRepo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class MessagesViewModel : ViewModel() {

    private val chatRepo = ChatRepo()
    private val userRepo = UserRepo()

    var currentChatID: MutableLiveData<String?> = MutableLiveData()
    var ownUserID = Firebase.auth.currentUser!!.uid
    private val _messages = MutableLiveData<List<Message>>()
    var messages = MutableLiveData<List<Message>>()

    val anotherUserData = MutableLiveData<User>()


    fun startChat(user1ID:String, user2ID:String){
        viewModelScope.launch {
            val existingChatID = chatRepo.checkExistingChat(user2ID)
            if (existingChatID != null){
                currentChatID.value = existingChatID
                getMessages(existingChatID)
            }else{
                val newChatID = chatRepo.createNewChat(user2ID)
                currentChatID.value = newChatID
                getMessages(newChatID)

            }
        }
    }

    fun getMessages(chatID:String){
        messages = chatRepo.getMessagesForChat(chatID)
        Log.e("viewmodelde: ", messages.toString())
    }

    fun sendMessage(chatID: String, msg:String, type: MessageType, callback: (Boolean) -> Unit){
        val message = Message(
            "",
            chatID,
            ownUserID,
            msg,
            System.currentTimeMillis(),
            type,
            false
        )

        chatRepo.sendMessage(message){ success ->
            if (success) callback(true)
            else callback(false)
        }
    }

    fun getAnotherUserData(userID:String): MutableLiveData<User> {
        userRepo.getUserData(userID)
        return userRepo.getUser()
    }

}