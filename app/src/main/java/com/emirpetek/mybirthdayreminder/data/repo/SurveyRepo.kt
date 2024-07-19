package com.emirpetek.mybirthdayreminder.data.repo

import com.emirpetek.mybirthdayreminder.data.entity.Survey
import com.google.firebase.database.FirebaseDatabase

class SurveyRepo {

    val dbRefSurvey = FirebaseDatabase.getInstance()
        .getReference("posts")
        .child("surveys")
        .child("survey")


    suspend fun insertSurvey(survey: Survey) : Boolean{
        return try {
            dbRefSurvey.push().setValue(survey)
            true
        }catch (e : Exception){
            false
        }
    }
}