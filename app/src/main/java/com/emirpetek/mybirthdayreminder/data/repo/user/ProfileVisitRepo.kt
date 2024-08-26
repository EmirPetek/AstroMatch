package com.emirpetek.mybirthdayreminder.data.repo.user

import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.user.ProfileVisit
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileVisitRepo {
    val dbRef = Firebase.firestore.collection("profileView").document("visitors")

    val querySize = MutableLiveData<Int>()
    val visitorList = MutableLiveData<List<ProfileVisit>>()

    fun insertProfileView(view: ProfileVisit){
        dbRef.collection(view.viewedID).document(view.visitorID).set(view)
    }

    fun getProfileViewerSize(){
        dbRef.collection(Firebase.auth.currentUser!!.uid).get().
                addOnSuccessListener { it ->
                    querySize.value = it.size()
                }
    }

    fun getProfileVisitors(){
        dbRef.collection(Firebase.auth.currentUser!!.uid)
            .get()
            .addOnSuccessListener { it ->
                val visitModel = it.toObjects(ProfileVisit::class.java)
                visitorList.value = visitModel
            }
    }

}