package com.emirpetek.mybirthdayreminder.data.repo

import com.emirpetek.mybirthdayreminder.data.entity.Question
import com.google.firebase.database.FirebaseDatabase

class QuestionRepo {

    val dbRefQuestions = FirebaseDatabase.getInstance().getReference("posts").child("questions").child("question")


    suspend fun insertQuestion(q : Question) : Boolean{
        return try {
            dbRefQuestions.push().setValue(q)
            true
        }catch (e : Exception){
            false
        }
    }

}