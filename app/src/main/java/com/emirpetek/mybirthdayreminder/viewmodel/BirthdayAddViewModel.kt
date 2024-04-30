package com.emirpetek.mybirthdayreminder.viewmodel

import androidx.lifecycle.ViewModel
import com.emirpetek.mybirthdayreminder.data.entity.Birthdays
import com.emirpetek.mybirthdayreminder.data.repo.BirthdaysDaoRepo

class BirthdayAddViewModel  : ViewModel() {

    val birthdaysDaoRepo = BirthdaysDaoRepo()
    fun insertBirthday(birthdate: Birthdays) {
        birthdaysDaoRepo.addBirthdays(birthdate)
    }
}
