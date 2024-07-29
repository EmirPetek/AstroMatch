package com.emirpetek.mybirthdayreminder.viewmodel.birthdays

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emirpetek.mybirthdayreminder.data.entity.Birthdays
import com.emirpetek.mybirthdayreminder.data.repo.birthdays.BirthdaysDaoRepo

class BirthdayUpdateViewModel : ViewModel() {

    var birthdayList = MutableLiveData<List<Birthdays>>()
    var birthdaysDaoRepo = BirthdaysDaoRepo()

    init {
        birthdayList = birthdaysDaoRepo.getBirthdays()
    }


    fun getBirthdayList(userID: String,birthdayKey:String) {
        birthdaysDaoRepo.getSpecialBirthdayData(userID,birthdayKey)
    }

    fun updateBirthday(userID: String, birthdayKey: String, birthday: Map<String, String>){
        birthdaysDaoRepo.updateBirthday(userID, birthdayKey, birthday)
    }

    fun deleteBirthday(userID: String,birthdayKey: String,deleteState: Map<String,Any>){
        birthdaysDaoRepo.deleteBirthday(userID,birthdayKey,deleteState)
    }
}