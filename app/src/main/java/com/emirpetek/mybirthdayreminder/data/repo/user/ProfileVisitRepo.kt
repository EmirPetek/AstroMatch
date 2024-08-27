package com.emirpetek.mybirthdayreminder.data.repo.user

import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.user.ProfileVisit
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProfileVisitRepo {
    private val dbRef = Firebase.firestore.collection("profileView").document("visitors")
    private val dbRefUser = Firebase.firestore.collection("users")

    val querySize = MutableLiveData<Int>()
    val visitorList = MutableLiveData<List<ProfileVisit>>()

    fun insertProfileView(view: ProfileVisit){
        dbRef.collection(view.viewedID).document().set(view)
    }

    fun getProfileViewerSize(){
        dbRef.collection(Firebase.auth.currentUser!!.uid).get().
                addOnSuccessListener { it ->
                    querySize.value = it.size()
                }
    }

    suspend fun getProfileVisitors(){
        CoroutineScope(Dispatchers.Main).launch{

        val currentUserID = Firebase.auth.currentUser?.uid!!
        val querySnapshot = dbRef.collection(currentUserID).get().await()
        val visitModel = querySnapshot.toObjects(ProfileVisit::class.java)
        getVisitorUser(visitModel)}
    }

    private suspend fun getVisitorUser(visitors: List<ProfileVisit>) {
        val visitList : List<ProfileVisit> = visitors
            for (visitor in visitors){
                val userSnapshot = dbRefUser.document(visitor.visitorID).get().await()
                val userModel = userSnapshot.toObject(User::class.java)
                if (userModel != null) {
                    visitor.user = userModel
                    visitList.plus(visitor)
                }
        }
        withContext(Dispatchers.Main) {
            visitorList.value = visitList
        }
    }

}