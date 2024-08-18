package com.emirpetek.mybirthdayreminder.viewmodel.earnGold

import androidx.lifecycle.ViewModel
import com.emirpetek.mybirthdayreminder.data.repo.CreditsRepo

class EarnGoldViewModel : ViewModel() {

    val creditRepo = CreditsRepo()

    fun incrementUserCredit(amount: Long){
        creditRepo.incrementUserCredit(amount)
    }

}