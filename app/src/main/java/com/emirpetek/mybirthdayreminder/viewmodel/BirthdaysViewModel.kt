package com.emirpetek.mybirthdayreminder.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emirpetek.mybirthdayreminder.data.entity.Birthdays
import com.emirpetek.mybirthdayreminder.data.repo.BirthdaysDaoRepo

class BirthdaysViewModel : ViewModel() {
    var birthdayList = MutableLiveData<List<Birthdays>>()

    // var birthdayKey : MutableLiveData<List<String>>
    var birthdaysDaoRepo = BirthdaysDaoRepo()

    init {
        birthdayList = birthdaysDaoRepo.getBirthdays()
        //birthdayKey = birthdaysDaoRepo.getBirthdayKey()
    }

    fun getBirthdayList(userID: String) {
        birthdaysDaoRepo.getBirthdaysData(userID)
    }

    fun insertBirthday(birthdate: Birthdays) {
        birthdaysDaoRepo.addBirthdays(birthdate)
    }


}
