package com.emirpetek.mybirthdayreminder.data.repo.like

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.like.Like
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LikeRepo {

    val dbRef = Firebase.firestore
        .collection("likes")
        //.collection(ilgili userID)
    val likeLiveData = MutableLiveData<List<Like>>()

    val dbUserRef = Firebase.firestore
        .collection("users")

    fun getLikeList() : MutableLiveData<List<Like>>{
        return likeLiveData
    }

    fun insertLikeUser(like: Like){
        dbRef
            .document("userList")
            .collection(like.receiverUserId)
            .document()
            .set(like)
    }

    suspend fun getLikes(userID: String) {
            val querySnapshot = dbRef
                .document("userList")
                .collection(userID)
                .get()
                .await()

            val likeModels = querySnapshot.documents.mapNotNull { document ->
                val like = document.toObject(Like::class.java)
                if (like?.deleteState == 0) {
                    like.likeID = document.id
                    like
                } else null
            }
            // Kullanıcı verilerini getir
            getUserDataFromLikeList(likeModels)
    }

    private suspend fun getUserDataFromLikeList(likes: List<Like>) {
        val likeListWithUsers = likes
            for (like in likes) {
                val userSnapshot = dbUserRef.document(like.senderUserId).get().await()
                val user = userSnapshot.toObject(User::class.java)
                if (user != null) {
                    like.user = user
                    likeListWithUsers.plus(like)  // Beğeniye ait kullanıcı verisini ekle
                }
            }

            withContext(Dispatchers.Main) {
                likeLiveData.value = likeListWithUsers
            }
    }


    fun deleteLike(likeID:String){
        dbRef
            .document("userList")
            .collection(Firebase.auth.currentUser!!.uid)
            .document(likeID)
            .update(
                mapOf(
                    "deleteState" to 1,
                    "deleteTime" to System.currentTimeMillis()
                )
            )

    }
}