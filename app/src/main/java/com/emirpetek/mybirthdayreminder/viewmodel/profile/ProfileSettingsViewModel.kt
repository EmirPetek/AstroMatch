package com.emirpetek.mybirthdayreminder.viewmodel.profile

import androidx.lifecycle.ViewModel
import com.emirpetek.mybirthdayreminder.data.entity.ContactFeedback
import com.emirpetek.mybirthdayreminder.data.repo.ContactFeedbackRepo

class ProfileSettingsViewModel : ViewModel() {
    val contactRepo = ContactFeedbackRepo()

    fun addFeedback(feedback: ContactFeedback){
        contactRepo.addFeedback(feedback)
    }
}