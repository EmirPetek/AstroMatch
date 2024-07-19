package com.emirpetek.mybirthdayreminder.viewmodel.social

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emirpetek.mybirthdayreminder.data.entity.Question
import com.emirpetek.mybirthdayreminder.data.entity.Survey
import com.emirpetek.mybirthdayreminder.data.repo.QuestionRepo
import com.emirpetek.mybirthdayreminder.data.repo.SurveyRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MakeSurveyViewModel : ViewModel() {
    private val repo = SurveyRepo()
    private val _surveyAdded = MutableStateFlow(false)
    val surveyAdded: StateFlow<Boolean> get() = _surveyAdded

    fun insertSurvey(survey: Survey){
        viewModelScope.launch {
            val result = repo.insertSurvey(survey)
            _surveyAdded.value = result
        }
    }
}