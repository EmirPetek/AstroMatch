package com.emirpetek.mybirthdayreminder.data.repo

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.emirpetek.mybirthdayreminder.data.entity.Post
import com.emirpetek.mybirthdayreminder.data.entity.Question
import com.emirpetek.mybirthdayreminder.data.entity.Survey
import com.emirpetek.mybirthdayreminder.viewmodel.social.AskQuestionViewModel
import com.emirpetek.mybirthdayreminder.viewmodel.social.MakeSurveyViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SocialPostRepo(
    val viewModelSurvey: MakeSurveyViewModel,
    val viewModelQuestion: AskQuestionViewModel,
    val viewLifecycleOwner: LifecycleOwner,
){

    var surveyList = ArrayList<Survey>()
    var questionList = ArrayList<Question>()


    fun getAllPost() : Post {

                viewModelQuestion.getQuestionList()
                viewModelSurvey.getSurveyList()






                viewModelQuestion.questionList.observe(viewLifecycleOwner, Observer { it ->
                    questionList = it as ArrayList
                    Log.e("questionList: ", it.toString() )

                })



                viewModelSurvey.surveyList.observe(viewLifecycleOwner, Observer { it ->
                    surveyList = it as ArrayList
                    Log.e("surveylist: ", it.toString() )

                })




        Log.e("socialpostrepo", "iki veri çekildikten sonra ")
       // val questionList = viewModelQuestion.questionList.value



        val post = Post(questionList, surveyList)
        return post
    }


}
