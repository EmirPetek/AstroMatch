package com.emirpetek.mybirthdayreminder.viewmodel.profile

import androidx.lifecycle.ViewModel
import com.emirpetek.mybirthdayreminder.data.entity.ContactFeedback
import com.emirpetek.mybirthdayreminder.data.repo.ContactFeedbackRepo
import com.emirpetek.mybirthdayreminder.data.repo.user.UserRepo
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileSettingsViewModel : ViewModel() {
    val contactRepo = ContactFeedbackRepo()
    val userRepo = UserRepo()

    fun addFeedback(feedback: ContactFeedback){
        contactRepo.addFeedback(feedback)
    }

    fun deleteUser(){
        userRepo.deleteUser(Firebase.auth.currentUser!!.uid)
    }
}