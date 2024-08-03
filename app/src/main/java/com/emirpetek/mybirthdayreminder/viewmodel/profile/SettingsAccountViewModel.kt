package com.emirpetek.mybirthdayreminder.viewmodel.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.data.repo.user.UserRepo

class SettingsAccountViewModel : ViewModel() {

    private val userRepo = UserRepo()
    var userData = MutableLiveData<User>()

    init {
        userData = userRepo.getUser()
    }

    fun getUserData(userID:String){
        userRepo.getUserData(userID)
    }

    fun updateUserData(user: User){
        userRepo.updateUser(user)
    }

}