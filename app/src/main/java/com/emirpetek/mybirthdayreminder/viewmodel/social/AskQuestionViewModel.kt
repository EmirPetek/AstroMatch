package com.emirpetek.mybirthdayreminder.viewmodel.social

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emirpetek.mybirthdayreminder.data.entity.Post
import com.emirpetek.mybirthdayreminder.data.entity.Question
import com.emirpetek.mybirthdayreminder.data.repo.social.QuestionRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AskQuestionViewModel : ViewModel() {
    private val repo = QuestionRepo()
    private val _questionAdded = MutableStateFlow(false)
    val questionAdded: StateFlow<Boolean> get() = _questionAdded
    var questionList = MutableLiveData<List<Post>>()


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

         }
    }
}