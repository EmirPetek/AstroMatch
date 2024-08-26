package com.emirpetek.mybirthdayreminder.viewmodel.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emirpetek.mybirthdayreminder.data.entity.user.ProfileVisit
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.data.repo.user.ProfileVisitRepo
import com.emirpetek.mybirthdayreminder.data.repo.user.UserRepo

class ProfileVisitorsViewModel : ViewModel() {
    val visitRepo = ProfileVisitRepo()
    var userRepo = UserRepo()

    var visitList = MutableLiveData<List<ProfileVisit>>()
    var user = MutableLiveData<User>()

    init {
        visitList = visitRepo.visitorList
        user = userRepo.getUserAsync()
    }

    fun getVisitorList(){
        visitRepo.getProfileVisitors()
    }

    fun getUser(userID:String){
        userRepo.getUserDataAsync(userID)
    }


}