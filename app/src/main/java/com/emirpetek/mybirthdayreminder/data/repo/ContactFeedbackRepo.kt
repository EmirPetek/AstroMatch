package com.emirpetek.mybirthdayreminder.data.repo

import com.emirpetek.mybirthdayreminder.data.entity.ContactFeedback
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ContactFeedbackRepo {

    private val dbRef = Firebase.firestore.collection("contactFeedback")

    fun addFeedback(feedback: ContactFeedback){
        val dataID = "${System.currentTimeMillis()}_${feedback.userID}_${dbRef.document().id}"
        dbRef.document(dataID).set(feedback)
    }
}