package com.emirpetek.mybirthdayreminder.viewmodel.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emirpetek.mybirthdayreminder.data.entity.User
import com.emirpetek.mybirthdayreminder.data.repo.UserRepo

class ProfileViewModel : ViewModel() {


    private val repo = UserRepo()
    var user = MutableLiveData<User>()// = MutableLiveData()
    var userFullname = MutableLiveData<String>()// = MutableLiveData()

    init {
        user = repo.getUser()
        userFullname = repo.getUserFullname()

    }

    fun getUser(userID:String){
        repo.getUserData(userID)
    }

    fun getUserFromUserID(userID: String){
        repo.getUserFullnameFromUserID(userID)
    }
}