package com.emirpetek.mybirthdayreminder.data.repo

import com.emirpetek.mybirthdayreminder.data.entity.Question
import com.google.firebase.database.FirebaseDatabase

class QuestionRepo {

    val dbRef = FirebaseDatabase.getInstance().getReference("posts").child("questions")


    suspend fun insertQuestion(q : Question) : Boolean{
        return try {
            dbRef.child("question").setValue(q)
            true
        }catch (e : Exception){
            false
        }
    }

}