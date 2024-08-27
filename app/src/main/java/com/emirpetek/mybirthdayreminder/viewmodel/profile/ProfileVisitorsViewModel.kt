package com.emirpetek.mybirthdayreminder.viewmodel.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emirpetek.mybirthdayreminder.data.entity.user.ProfileVisit
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.data.repo.user.ProfileVisitRepo
import com.emirpetek.mybirthdayreminder.data.repo.user.UserRepo
import kotlinx.coroutines.launch

class ProfileVisitorsViewModel : ViewModel() {
    val visitRepo = ProfileVisitRepo()

    var visitList = MutableLiveData<List<ProfileVisit>>()

    init {
        visitList = visitRepo.visitorList
    }

    fun getVisitorList(){
        viewModelScope.launch {
            visitRepo.getProfileVisitors()
        }
    }



}