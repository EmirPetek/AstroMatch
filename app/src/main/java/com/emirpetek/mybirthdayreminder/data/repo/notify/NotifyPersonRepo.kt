package com.emirpetek.mybirthdayreminder.data.repo.notify

import com.emirpetek.mybirthdayreminder.data.entity.notify.NotifyPerson
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NotifyPersonRepo {


    val dbRef = Firebase.firestore
        .collection("notifyPerson")
        .document("notifiedPersons")


    fun insertNotify(notify: NotifyPerson){
        val docID = "${notify.timestamp}_${notify.reporterUserID}"
        dbRef.collection(notify.reportedUserID).document(docID).set(notify)
    }
}