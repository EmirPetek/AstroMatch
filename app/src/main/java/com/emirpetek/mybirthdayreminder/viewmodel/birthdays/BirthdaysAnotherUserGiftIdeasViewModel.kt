package com.emirpetek.mybirthdayreminder.viewmodel.birthdays

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emirpetek.mybirthdayreminder.data.entity.birthdays.BirthdayGiftIdea
import com.emirpetek.mybirthdayreminder.data.repo.birthdays.BirthdaysDaoRepo
import kotlinx.coroutines.launch

class BirthdaysAnotherUserGiftIdeasViewModel : ViewModel() {
    private val birthdaysRepo = BirthdaysDaoRepo()
    var giftIdeaList = MutableLiveData<List<BirthdayGiftIdea>>()

    init {
        giftIdeaList = birthdaysRepo.giftIdeaList
    }

    fun getGiftIdeas(){
        viewModelScope.launch {
            birthdaysRepo.getBirthdayGiftIdeas()
        }
    }

}