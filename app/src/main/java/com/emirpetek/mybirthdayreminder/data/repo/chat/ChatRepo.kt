package com.emirpetek.mybirthdayreminder.data.repo.chat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.chat.Chat
import com.emirpetek.mybirthdayreminder.data.entity.chat.Message
import com.emirpetek.mybirthdayreminder.data.entity.chat.UserChats
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ChatRepo {

    val ownUserID = Firebase.auth.currentUser!!.uid
    val userChatsRef = FirebaseDatabase.getInstance().getReference("userChats")//.child(ownUserID)
    val chatsRef = FirebaseDatabase.getInstance().getReference("chats")

    private val messagesRef = FirebaseDatabase.getInstance().getReference("messages")
    private val messagesLiveData = MutableLiveData<List<Message>>()


    suspend fun checkExistingChat(anotherUserID:String) : String? {
        return try {
            val snapshot = userChatsRef.child(ownUserID).child(anotherUserID).get().await()
            snapshot.getValue(String::class.java)
        }catch (e: Exception){
            null
        }
    }

    suspend fun createNewChat(anotherUserID: String): String{
        val newChatId = chatsRef.push().key!!

        val newChat = Chat(
            chatID = newChatId,
            participants = mapOf(ownUserID to true, anotherUserID to true),
            createTime = System.currentTimeMillis(),
            deleteState = 0,
            deleteTime = 0,
            lastMessage = "",
            lastMessageTimestamp = 0,
            unreadCount = mapOf(ownUserID to 0, anotherUserID to 0)
        )

        chatsRef.child(newChatId).setValue(newChat).await()

        userChatsRef.child(ownUserID).child(anotherUserID).setValue(newChatId).await()
        userChatsRef.child(anotherUserID).child(ownUserID).setValue(newChatId).await()

        return newChatId
    }



    fun getMessagesForChat(chatID:String) : MutableLiveData<List<Message>> {
        messagesRef.child(chatID).limitToLast(25).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = snapshot.children.mapNotNull {
                    it.getValue(Message::class.java)
                }
                Log.e("repodaÇ ", messages.toString())
                messagesLiveData.value = messages
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        return messagesLiveData
    }

    fun sendMessage(message: Message,callback: (Boolean) -> Unit){
        val messageID = messagesRef.child(message.chatID).push().key!!
        val messageWithId = message.copy(messageID = messageID)

        messagesRef.child(message.chatID).child(messageID).setValue(messageWithId)
            .addOnSuccessListener {
                updateLastMessage(messageWithId.chatID,messageWithId)
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }

    }

    private fun updateLastMessage(chatId: String, message: Message) {
        chatsRef.child(chatId).updateChildren(mapOf(
            "lastMessage" to message.messageText,
            "lastMessageTimestamp" to message.timestamp,
            "unreadCount/${message.senderID}" to 0 // Gönderen için okunmamış mesaj sayısını sıfırlar
        ))
    }



}