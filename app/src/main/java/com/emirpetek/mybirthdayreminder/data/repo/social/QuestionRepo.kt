package com.emirpetek.mybirthdayreminder.data.repo.social

import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.Post
import com.emirpetek.mybirthdayreminder.data.entity.Question
import com.emirpetek.mybirthdayreminder.data.entity.QuestionAnswers
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class QuestionRepo {

    val dbRefQuestions = FirebaseDatabase.getInstance()
        .getReference("posts")
        .child("question")

    val dbRefQuestionAnswer = FirebaseDatabase.getInstance()
        .getReference("posts")
        .child("questionAnswers")

    val questionList: MutableLiveData<List<Post>> = MutableLiveData()



    suspend fun insertQuestion(q : Post) : Boolean{
        return try {
            dbRefQuestions.push().setValue(q)
            true
        }catch (e : Exception){
            false
        }
    }

    fun getQuestionList(){
        dbRefQuestions
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var questionl = ArrayList<Post>()
                    for (q in snapshot.children){
                        val questionModel = q.getValue(Post::class.java)!!
                        if (questionModel.deleteState.equals("0")){
                            questionModel.postID= q.key!!
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

    fun insertQuestionAnswer(q: QuestionAnswers): Boolean{
        return try {
            dbRefQuestionAnswer.child(q.postID).push().setValue(q)
            true
        }catch (e: Exception){
            false
        }
    }

}