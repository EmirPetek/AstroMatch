package com.emirpetek.mybirthdayreminder.data.repo.like

import com.emirpetek.mybirthdayreminder.data.entity.like.Like
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LikeRepo {

    val dbRef = Firebase.firestore
        .collection("likes")
        //.collection(ilgili userID)

    fun insertLikeUser(like: Like){
        dbRef
            .document("userList")
            .collection(like.receiverUserId)
            .document()
            .set(like)
    }
}