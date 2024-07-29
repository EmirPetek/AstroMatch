package com.emirpetek.mybirthdayreminder.viewmodel.birthdays

import androidx.lifecycle.ViewModel
import com.emirpetek.mybirthdayreminder.data.entity.Birthdays
import com.emirpetek.mybirthdayreminder.data.repo.birthdays.BirthdaysDaoRepo

class BirthdayAddViewModel  : ViewModel() {

    val birthdaysDaoRepo = BirthdaysDaoRepo()

    fun insertBirthday(birthdays: Birthdays){
        birthdaysDaoRepo.insertBirthdayFS(birthdays)
    }
}
