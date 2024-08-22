package com.emirpetek.mybirthdayreminder.data.repo.like

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.like.Like
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LikeRepo {

    val dbRef = Firebase.firestore
        .collection("likes")
        //.collection(ilgili userID)
    val likeLiveData = MutableLiveData<List<Like>>()

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

    fun getLikes(userID: String) {
        val listLike = ArrayList<Like>()
        dbRef
            .document("userList")
            .collection(userID)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val likeModels = querySnapshot.documents.mapNotNull { it.toObject(Like::class.java) }
                Log.e("like models: ", likeModels.toString())
                likeLiveData.value = likeModels
            }
            .addOnFailureListener { exception ->
                Log.e("Error getting likes", exception.toString())
            }
           // likeLiveData.value = listLike

    }
}