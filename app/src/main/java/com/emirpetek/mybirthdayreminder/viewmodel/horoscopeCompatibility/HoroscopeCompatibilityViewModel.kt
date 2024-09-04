package com.emirpetek.mybirthdayreminder.viewmodel.horoscopeCompatibility

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emirpetek.mybirthdayreminder.data.entity.UserCredits
import com.emirpetek.mybirthdayreminder.data.entity.horoscopeCompatibility.CompatibilityAnalysis
import com.emirpetek.mybirthdayreminder.data.repo.CreditsRepo
import com.emirpetek.mybirthdayreminder.data.repo.horoscopeCompatibility.HoroscopeCompatibilityRepo

class HoroscopeCompatibilityViewModel : ViewModel() {

    private val horoscopeRepo = HoroscopeCompatibilityRepo()
    private val creditRepo = CreditsRepo()

    var compatibilityResponse : MutableLiveData<String> = MutableLiveData()
    var credit : MutableLiveData<UserCredits> = MutableLiveData()
    var compatibilityList : MutableLiveData<List<CompatibilityAnalysis>> = MutableLiveData<List<CompatibilityAnalysis>>()


    init {
        compatibilityResponse = horoscopeRepo.getAIResponse()
        credit = creditRepo.getCreditAmount()
        compatibilityList = horoscopeRepo.getCompatibilityResultList()
    }

    fun getCompatibility(query: String, tokenNumber: Int){
        horoscopeRepo.getOpenAIResponse(query,tokenNumber)
    }

    fun getCredit(){
        creditRepo.getUserCreditsAmountFun()
    }

    fun decrementUserCredit(amount: Long){
        creditRepo.decrementUserCredit(amount)
    }

    fun saveCompatibilityResult(analysis: CompatibilityAnalysis){
        horoscopeRepo.saveCompatibilityReport(analysis)
    }

    fun getCompatibilityReportList(){
        horoscopeRepo.getCompatibilityReportList()
    }
}