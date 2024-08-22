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
   // val chatDataList : ArrayList<Chat> = arrayListOf()

    companion object {
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
        val chatList = ArrayList<UserChats>()
        userChatsRef.child(userID).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
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
        val chatDataList = ArrayList<Chat>()
        val chatCount = chatList.size

        if (chatCount == 0) {
            chatListLiveData.value = chatDataList
            return
        }
        for (c in chatList){
            chatDataList.clear()

            chatsRef.child(c.chatID).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    val chat = snapshot.getValue(Chat::class.java)!!
                    Log.e("chatttt", chat.toString())
                    loadUsersForChat(chat,chatCount,chatDataList)
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

    private fun loadUsersForChat(chat: Chat, chatCount: Int,chatDataList: ArrayList<Chat>) {
        val participants = chat.participants

            val firstParticipant = participants.entries.toList()[0].key.toString()
            val secondParticipant = participants.entries.toList()[1].key.toString()
            var anotherUserID = if (firstParticipant.equals(ownUserID)) secondParticipant
            else firstParticipant

        dbRefUser.document(anotherUserID).get().addOnSuccessListener { snapshot ->
            val userModel = snapshot.toObject(com.emirpetek.mybirthdayreminder.data.entity.user.User::class.java)!!
            chat.user = userModel


            // Yeni gelen mesajlar için güncelleme kontrolü.

            // Liste içinde aynı chatID'ye sahip bir nesne var mı diye kontrol et
            val existingChatIndex = chatDataList.indexOfFirst { it.chatID == chat.chatID }
            if (existingChatIndex != -1) {
                // Eğer varsa, eski nesneyi güncellenmiş nesneyle değiştir
                chatDataList[existingChatIndex] = chat
            } else {
                // Eğer yoksa, listeye yeni nesneyi ekle
                chatDataList.add(chat)
            }


            loadedChatCount++
            chatListLiveData.value = chatDataList

            if (loadedChatCount == chatCount) {
                loadedChatCount = 0
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
            lastMessageTimestamp = System.currentTimeMillis(),
            unreadCount = mapOf(ownUserID to 0, anotherUserID to 0)
        )

        chatsRef.child(newChatId).setValue(newChat).await()

        userChatsRef.child(ownUserID).child(anotherUserID).setValue(newChatId).await()
        userChatsRef.child(anotherUserID).child(ownUserID).setValue(newChatId).await()

        return newChatId
    }



    fun getMessagesForChat(chatID:String) : MutableLiveData<List<Message>> {
        messagesRef.child(chatID).limitToLast(50).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = snapshot.children.mapNotNull {
                    it.getValue(Message::class.java)
                }
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