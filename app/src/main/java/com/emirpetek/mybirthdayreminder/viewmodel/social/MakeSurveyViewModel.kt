package com.emirpetek.mybirthdayreminder.viewmodel.social

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emirpetek.mybirthdayreminder.data.entity.question.Post
import com.emirpetek.mybirthdayreminder.data.entity.question.SelectedOptions
import com.emirpetek.mybirthdayreminder.data.repo.social.SurveyRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MakeSurveyViewModel : ViewModel() {
    private val repo = SurveyRepo()
    private val _surveyAdded = MutableStateFlow(false)
    val surveyAdded: StateFlow<Boolean> get() = _surveyAdded
    var surveyList = MutableLiveData<List<Post>>()
    var surveyOptionList = MutableLiveData<List<SelectedOptions>>()
    private val _surveyOptionAdded = MutableStateFlow(false)
    val surveyOptionAdded: StateFlow<Boolean> get() = _surveyOptionAdded

    init {
        surveyList = repo.surveyList
        surveyOptionList = repo.surveyOption
    }

    fun insertSurvey(survey: Post){
        viewModelScope.launch {
            val result = repo.insertSurvey(survey)
            _surveyAdded.value = result
        }
    }

     fun getSurveyList(){
        viewModelScope.launch {
            repo.getSurvey()

        }
    }

    fun insertSelectedSurvey(selectedOptions: SelectedOptions){
        viewModelScope.launch {
            repo.insertSelectedOption(selectedOptions)
           // _surveyOptionAdded.value = result
        }
    }

    fun getSelectedSurveyUsers(postID: String){
        viewModelScope.launch {
            repo.checkSelectedOptions(postID)
        }
    }
}