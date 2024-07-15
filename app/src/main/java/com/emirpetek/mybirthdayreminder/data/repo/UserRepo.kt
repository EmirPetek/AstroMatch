package com.emirpetek.mybirthdayreminder.data.repo

import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.User
import com.google.firebase.database.FirebaseDatabase

class UserRepo {

    private val user : MutableLiveData<User> = MutableLiveData()
    private val dbRef = FirebaseDatabase.getInstance().getReference("users")


    suspend fun addUser(user: User) : Boolean{

        return try {
            dbRef.push().setValue(user)
            true
        }catch (e : Exception){
            false
        }
    }

}