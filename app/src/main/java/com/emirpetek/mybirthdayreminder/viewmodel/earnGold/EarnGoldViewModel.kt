package com.emirpetek.mybirthdayreminder.viewmodel.earnGold

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emirpetek.mybirthdayreminder.data.entity.UserCredits
import com.emirpetek.mybirthdayreminder.data.repo.CreditsRepo

class EarnGoldViewModel : ViewModel() {

    val creditRepo = CreditsRepo()
    var credit = MutableLiveData<UserCredits>()

    init {
        credit = creditRepo.getCreditAmount()
    }

    fun incrementUserCredit(amount: Long){
        creditRepo.incrementUserCredit(amount)
    }

    fun getCreditData(){
        creditRepo.getUserCreditsAmountFun()
    }

}