package com.emirpetek.mybirthdayreminder.viewmodel.matchPerson

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.data.repo.CreditsRepo
import com.emirpetek.mybirthdayreminder.data.repo.user.UserRepo

class MatchPersonViewModel : ViewModel() {

    private val userRepo = UserRepo()
    private val creditRepo = CreditsRepo()

    var user = MutableLiveData<ArrayList<User>>()
    var credit = MutableLiveData<Long>()

    init {
        user = userRepo.getCompatibleUsers()
        credit = creditRepo.getCreditAmount()
    }

    fun getCompatibleUsersData(userID:String){
        userRepo.getCompatibleUsersData(userID)
    }

    fun getUserCreditsAmount(){
        creditRepo.getUserCreditsAmountFun()
    }


}