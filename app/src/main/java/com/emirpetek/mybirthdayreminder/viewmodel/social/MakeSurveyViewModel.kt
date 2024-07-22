package com.emirpetek.mybirthdayreminder.viewmodel.social

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emirpetek.mybirthdayreminder.data.entity.Post
import com.emirpetek.mybirthdayreminder.data.entity.Survey
import com.emirpetek.mybirthdayreminder.data.repo.social.SurveyRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MakeSurveyViewModel : ViewModel() {
    private val repo = SurveyRepo()
    private val _surveyAdded = MutableStateFlow(false)
    val surveyAdded: StateFlow<Boolean> get() = _surveyAdded
    var surveyList = MutableLiveData<List<Post>>()


    init {
        surveyList = repo.surveyList
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
}