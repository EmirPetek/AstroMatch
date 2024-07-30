package com.emirpetek.mybirthdayreminder.viewmodel.social

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emirpetek.mybirthdayreminder.data.entity.question.Post
import com.emirpetek.mybirthdayreminder.data.entity.question.QuestionAnswers
import com.emirpetek.mybirthdayreminder.data.repo.social.QuestionRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AskQuestionViewModel : ViewModel() {
    private val repo = QuestionRepo()
    private val _questionAdded = MutableStateFlow(false)
    val questionAdded: StateFlow<Boolean> get() = _questionAdded
    var questionList = MutableLiveData<List<Post>>()

    private val _questionAnswerAdded = MutableStateFlow(false)
    val questionAnswerAdded: StateFlow<Boolean> get() = _questionAnswerAdded

    private var isDataLoaded = false
    var isNewCome = false

    init {
        questionList = repo.questionList
    }


    fun insertQuestion(q: Post){
        viewModelScope.launch {
            val result = repo.insertQuestion(q)
            _questionAdded.value = result
        }
    }

      fun getQuestionList(){
         viewModelScope.launch {
             repo.getQuestionList()
             isDataLoaded = true

         }
    }

    fun insertQuestionAnswer(q: QuestionAnswers){
        viewModelScope.launch {
            val result = repo.insertQuestionAnswer(q)
            _questionAnswerAdded.value = result
        }
    }

    fun refreshQuestionList() {
        isDataLoaded = false
        getQuestionList()
    }
}