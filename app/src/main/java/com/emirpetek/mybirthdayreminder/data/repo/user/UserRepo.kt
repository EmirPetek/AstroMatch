package com.emirpetek.mybirthdayreminder.data.repo.user

import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRepo {

    private var user: MutableLiveData<User> = MutableLiveData()
    private var userFullname: MutableLiveData<String> = MutableLiveData()
    private var userImageURL: MutableLiveData<String> = MutableLiveData()
    private val dbRef = Firebase.firestore.collection("users")

    fun getUser(): MutableLiveData<User> {
        return user
    }

    fun getUserFullname(): MutableLiveData<String> {
        return userFullname
    }

    fun getUserImage(): MutableLiveData<String> {
        return userImageURL
    }

    suspend fun addUser(user: User): Boolean {
        return try {
            dbRef.document(user.userID).set(user)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getUserData(userID: String) {
        dbRef.document(userID).addSnapshotListener { snapshot, e ->
            if (e != null) {
                // Handle error
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val userModel = snapshot.toObject(User::class.java)!!
                user.value = userModel
            }
        }
    }

    fun getUserFullnameFromUserID(userID: String) {
        dbRef.document(userID).addSnapshotListener { snapshot, e ->
            if (e != null) {
                // Handle error
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val userModel = snapshot.toObject(User::class.java)!!
                val fullname = userModel.fullname
                val userImg = userModel.profile_img
                userFullname.value = fullname
                userImageURL.value = userImg
            }
        }
    }

    fun updateUser(userID: String, userUpdates: Map<String, Any>) {
        dbRef.document(userID).update(userUpdates)
            .addOnSuccessListener {
                // Log success or handle accordingly
            }
            .addOnFailureListener { e ->
                // Log error or handle accordingly
            }
    }

    fun deleteUser(userID: String) {
        dbRef.document(userID).delete()
            .addOnSuccessListener {
                // Log success or handle accordingly
            }
            .addOnFailureListener { e ->
                // Log error or handle accordingly
            }
    }
}
