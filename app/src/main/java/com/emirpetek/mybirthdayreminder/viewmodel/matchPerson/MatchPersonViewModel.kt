package com.emirpetek.mybirthdayreminder.viewmodel.matchPerson

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.data.repo.user.UserRepo

class MatchPersonViewModel : ViewModel() {

    private val repository = UserRepo()

    var user = MutableLiveData<ArrayList<User>>()

    init {
        user = repository.getCompatibleUsers()
    }

    fun getCompatibleUsersData(userID:String){
        repository.getCompatibleUsersData(userID)
    }


}