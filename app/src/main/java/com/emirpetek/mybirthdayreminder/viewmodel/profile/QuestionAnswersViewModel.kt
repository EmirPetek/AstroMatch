package com.emirpetek.mybirthdayreminder.viewmodel.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emirpetek.mybirthdayreminder.data.entity.QuestionAnswers
import com.emirpetek.mybirthdayreminder.data.repo.social.QuestionRepo

class QuestionAnswersViewModel : ViewModel() {
    val questionRepo = QuestionRepo()

    var answerList = MutableLiveData<List<QuestionAnswers>>()

    init {
        answerList = questionRepo.answerList
    }


    fun getAnswers(postID:String){
        questionRepo.getQuestionAnswers(postID)
    }

    fun deletePost(postID: String){
        questionRepo.deletePost(postID)
    }



}