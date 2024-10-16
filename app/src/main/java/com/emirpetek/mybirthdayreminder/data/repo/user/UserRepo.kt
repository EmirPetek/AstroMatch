package com.emirpetek.mybirthdayreminder.data.repo.user

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.user.UserGalleryPhoto
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.data.entity.user.userFilter.UserFilter
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserRepo {

    private var user: MutableLiveData<User> = MutableLiveData()
    private var userAsync: MutableLiveData<User> = MutableLiveData()
    private var compatibleUserList: MutableLiveData<ArrayList<User>> = MutableLiveData()
    private var userZodiac: MutableLiveData<Int> = MutableLiveData()
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

    fun getUserAsync() : MutableLiveData<User> {
        return userAsync
    }

    fun getCompatibleUsers(): MutableLiveData<ArrayList<User>>{
        return compatibleUserList
    }

    fun getUserZodiac(): MutableLiveData<Int> {
        return userZodiac
    }

    suspend fun addUser(user: User): Boolean {
        return try {
            dbRef.document(user.userID).set(user)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun     getUserData(userID: String) {
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

    fun getCompatibleUsersData(userID: String){
        val userList = ArrayList<User>()
        dbRef.whereNotEqualTo(FieldPath.documentId(),userID).get()
            .addOnSuccessListener { document ->
                for (u in document){
                    val userModel = u.toObject(User::class.java)
                    if (userModel.accountDeleteState.equals("0")){
                        userList.add(userModel)
                    }
                }
                compatibleUserList.value = userList
            }
    }

    fun getUserDataAsync(userID: String) {
        dbRef.document(userID).get().addOnSuccessListener { snapshot ->
            val userModel = snapshot.toObject(User::class.java)!!
            userAsync.value = userModel
        }
        /*dbRef.document(userID).addSnapshotListener { snapshot, e ->
            if (e != null) {
                // Handle error
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val userModel = snapshot.toObject(User::class.java)!!
                userAsync.value = userModel
            }
        }*/
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

    fun getUserZodiac(userID: String){
        dbRef.document(userID).addSnapshotListener{ snapshot, e ->
            if (e != null) {
                // Handle error
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val userModel = snapshot.toObject(User::class.java)!!
                val zodiac = userModel.zodiac
                userZodiac.value = zodiac
            }
        }

    }

    fun updateUser(user:User) {
        dbRef.document(user.userID).set(user)
            .addOnSuccessListener {
                Log.e("updateUser: ", "Successfully updated!")
            }
            .addOnFailureListener { e ->
                Log.e("updateUser: ", "Failed in updating!")
            }
    }

    suspend fun insertProfileGalleryURLs(userID:String, imgList: ArrayList<UserGalleryPhoto>): Boolean{
        //val data = hashMapOf("profileGalleryPhotos" to imgList)

        return try {
            val updates = hashMapOf<String, Any>(
                "profileGalleryPhotos" to FieldValue.arrayUnion(*imgList.toTypedArray())
            )

            dbRef.document(userID).update(updates)
                .addOnSuccessListener {
                    Log.e("insertProfileGalleryURLs: ", "Successfully inserted!")
                }
                .addOnFailureListener { e ->
                    Log.e("insertProfileGalleryURLs: ", "Failed in inserting!", e)
                }
            true
        }catch (e : Exception){
            Log.e("insertProfileGalleryURLs exception: ", e.toString())
            false
        }
    }

    fun deleteUser(userID: String) {
        val deleteMap = mapOf(
            "accountDeleteState" to "1"
        )
        dbRef.document(userID).update(deleteMap)
            .addOnSuccessListener {

            }
            .addOnFailureListener { e ->
                // Log error or handle accordingly
            }
    }


}
