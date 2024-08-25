package com.emirpetek.mybirthdayreminder.data.repo.user

import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.user.ProfileView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ProfileViewRepo {
    val dbRef = Firebase.firestore.collection("profileView").document("visitors")

    val querySize = MutableLiveData<Int>()

    fun insertProfileView(view: ProfileView){
        dbRef.collection(view.viewedID).document(view.visitorID).set(view)
    }

    fun getProfileViewerSize(){
        dbRef.collection(Firebase.auth.currentUser!!.uid).get().
                addOnSuccessListener { it ->
                    querySize.value = it.size()
                }
    }

}