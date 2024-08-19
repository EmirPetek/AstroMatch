package com.emirpetek.mybirthdayreminder.viewmodel.matchPerson

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emirpetek.mybirthdayreminder.data.entity.UserCredits
import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.data.repo.CreditsRepo
import com.emirpetek.mybirthdayreminder.data.repo.user.UserRepo

class MatchPersonViewModel : ViewModel() {

    private val userRepo = UserRepo()
    private val creditRepo = CreditsRepo()

    var user = MutableLiveData<ArrayList<User>>()
    var credit = MutableLiveData<UserCredits>()
    var userOwnZodiac = MutableLiveData<Int>()

    init {
        user = userRepo.getCompatibleUsers()
        credit = creditRepo.getCreditAmount()
        userOwnZodiac = userRepo.getUserZodiac()
    }

    fun getCompatibleUsersData(userID:String){
        userRepo.getCompatibleUsersData(userID)
    }

    fun getUserCreditsAmount(){
        creditRepo.getUserCreditsAmountFun()
    }

    fun getOwnUserZodiac(userID:String){
        userRepo.getUserZodiac(userID)
    }

    fun decrementUserCredit(amount: Long){
        creditRepo.decrementUserCredit(amount)
    }

    fun decrementLikeRight(amount: Long){
        creditRepo.decrementLikeRight(amount)
    }

    fun decrementMegaLikeRight(amount: Long){
        creditRepo.decrementMegaLikeRight(amount)
    }

    fun setDailyBonusValues(){
        creditRepo.setDailyBonusValues()
    }


}