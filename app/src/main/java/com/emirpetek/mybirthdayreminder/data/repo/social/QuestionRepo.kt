package com.emirpetek.mybirthdayreminder.data.repo.social

import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.Question
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class QuestionRepo {

    val dbRefQuestions = FirebaseDatabase.getInstance()
        .getReference("posts")
        .child("questions")
        .child("question")

    val questionList: MutableLiveData<List<Question>> = MutableLiveData()



    suspend fun insertQuestion(q : Question) : Boolean{
        return try {
            dbRefQuestions.push().setValue(q)
            true
        }catch (e : Exception){
            false
        }
    }

    fun getQuestionList(){
        dbRefQuestions
            .orderByChild("question")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var questionl = ArrayList<Question>()
                    for (q in snapshot.children){
                        val questionModel = q.getValue(Question::class.java)!!
                        if (questionModel.deleteState.equals("0")){
                            questionl.add(questionModel)
                        }
                    }
                    questionList.value = questionl
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

}