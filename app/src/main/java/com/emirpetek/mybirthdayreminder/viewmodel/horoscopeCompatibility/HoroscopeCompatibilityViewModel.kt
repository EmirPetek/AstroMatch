package com.emirpetek.mybirthdayreminder.viewmodel.horoscopeCompatibility

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emirpetek.mybirthdayreminder.data.repo.horoscopeCompatibility.HoroscopeCompatibilityRepo

class HoroscopeCompatibilityViewModel : ViewModel() {

    private val horoscopeRepo = HoroscopeCompatibilityRepo()

    var compatibilityResponse : MutableLiveData<String> = MutableLiveData()

    init {
        compatibilityResponse = horoscopeRepo.getAIResponse()
    }

    fun getCompatibility(query: String){
        horoscopeRepo.getOpenAIResponse(query)
    }
}