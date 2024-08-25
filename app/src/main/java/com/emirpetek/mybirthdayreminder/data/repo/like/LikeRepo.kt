package com.emirpetek.mybirthdayreminder.data.repo.like

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.like.Like
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LikeRepo {

    val dbRef = Firebase.firestore
        .collection("likes")
        //.collection(ilgili userID)
    val likeLiveData = MutableLiveData<List<Like>>()
    val likeWithUserLiveData = MutableLiveData<List<Like>>()

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

    fun getLikes(userID: String) {
        val listLike = ArrayList<Like>()
        dbRef
            .document("userList")
            .collection(userID)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val likeModels = querySnapshot.documents.mapNotNull { it ->
                    val like = it.toObject(Like::class.java)
                    if (like?.deleteState?.equals(0) == true) {
                        like.likeID = it.id
                        like
                    }else null
                }
                //Log.e("like models: ", likeModels.toString())
                likeLiveData.value = likeModels
            }
            .addOnFailureListener { exception ->
                Log.e("Error getting likes", exception.toString())
            }
           // likeLiveData.value = listLike
    }

    fun getUserDataFromLikeList(list : List<Like>){
        val likeList = list
        for (like in likeList){
            dbUserRef.document(like.senderUserId).get()
                .addOnSuccessListener { snapshot ->
                    val user = snapshot.toObject(User::class.java)
                    like.user = user
                    likeWithUserLiveData.value = likeList
                    //Log.e("userrrr", "${user?.userID} ve like içindeki uid ${like.senderUserId}")
                }
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