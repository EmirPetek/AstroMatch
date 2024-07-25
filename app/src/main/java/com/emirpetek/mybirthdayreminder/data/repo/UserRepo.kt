package com.emirpetek.mybirthdayreminder.data.repo

import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRepo {

    private var user : MutableLiveData<User> = MutableLiveData()
    private var userFullname : MutableLiveData<String> = MutableLiveData()
    private var userImageURL : MutableLiveData<String> = MutableLiveData()
    private val dbRef = FirebaseDatabase.getInstance().getReference("users")


    fun getUser() : MutableLiveData<User>{
        return user
    }

    fun getUserFullname() : MutableLiveData<String>{
        return userFullname
    }
    fun getUserImage() : MutableLiveData<String>{
        return userImageURL
    }

    suspend fun addUser(user: User) : Boolean{

        return try {
            dbRef.child(user.userID).setValue(user)
            true
        }catch (e : Exception){
            false
        }
    }

    fun getUserData(userID:String) {
        dbRef.orderByChild(userID).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var userModel : User = User()
                for (i in snapshot.children){
                    userModel = i.getValue(User::class.java)!!
                }
                user.value = userModel
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }


    fun getUserFullnameFromUserID(userID: String){
        dbRef.orderByChild(userID).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var userModel : User = User()
                var fullname: String = String()
                var userImg: String = String()
                for (i in snapshot.children){
                    userModel = i.getValue(User::class.java)!!
                    fullname = userModel.fullname
                    userImg = userModel.profile_img
                }
                userFullname.value = fullname
                userImageURL.value = userImg
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }



}