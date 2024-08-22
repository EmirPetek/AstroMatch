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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ChatRepo {

    private val dbRefUser = Firebase.firestore.collection("users")

    val ownUserID = Firebase.auth.currentUser!!.uid
    val userChatsRef = FirebaseDatabase.getInstance().getReference("userChats")//.child(ownUserID)
    val chatsRef = FirebaseDatabase.getInstance().getReference("chats")

    private val messagesRef = FirebaseDatabase.getInstance().getReference("messages")
    private val messagesLiveData = MutableLiveData<List<Message>>()
    val chatIDList = MutableLiveData<ArrayList<UserChats>>()
    var chatListLiveData = MutableLiveData<ArrayList<Chat>>()

    companion object {
        val chatDataList : ArrayList<Chat> = arrayListOf()
        var loadedChatCount = 0
    }




    suspend fun checkExistingChat(anotherUserID:String) : String? {
        return try {
            val snapshot = userChatsRef.child(ownUserID).child(anotherUserID).get().await()
            snapshot.getValue(String::class.java)
        }catch (e: Exception){
            null
        }
    }

    fun getChatIDs(userID:String){
        userChatsRef.child(userID).addValueEventListener(object : ValueEventListener{
            val chatList = ArrayList<UserChats>()
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children){
                    val chatID = i.getValue(String::class.java)!!
                    val chatObj = UserChats(i.key!!,chatID)
                    chatList.add(chatObj)
                }
                chatIDList.value = chatList
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getChatData(chatList: ArrayList<UserChats>){
        var loadedChatCount = 0
        val chatCount = chatList.size

        if (chatCount == 0) {
            chatListLiveData.value = chatDataList
            return
        }

        for (c in chatList){
            chatsRef.child(c.chatID).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val chat = snapshot.getValue(Chat::class.java)!!
                    loadUsersForChat(chat,chatCount)
                   /* chatDataList.add(chat)
                    Companion.loadedChatCount++

                    if (Companion.loadedChatCount == chatCount) {
                        Log.e("chatlisfsd", chatDataList.toString())
                        chatListLiveData.value = chatDataList
                    }*/
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

    }

    private fun loadUsersForChat(chat: Chat, chatCount: Int) {
        val participants = chat.participants

            val firstParticipant = participants.entries.toList()[0].key.toString()
            val secondParticipant = participants.entries.toList()[1].key.toString()
            var anotherUserID = if (firstParticipant.equals(ownUserID)) secondParticipant
            else firstParticipant

        dbRefUser.document(anotherUserID).get().addOnSuccessListener { snapshot ->
            val userModel = snapshot.toObject(com.emirpetek.mybirthdayreminder.data.entity.user.User::class.java)!!
            chat.user = userModel
            chatDataList.add(chat)
            loadedChatCount++

            if (loadedChatCount == chatCount) {
                Log.e("chatlisfsd", chatDataList.toString())
                chatListLiveData.value = chatDataList
            }
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