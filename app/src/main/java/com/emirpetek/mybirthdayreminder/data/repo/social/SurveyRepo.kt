package com.emirpetek.mybirthdayreminder.data.repo.social

import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.Post
import com.emirpetek.mybirthdayreminder.data.entity.Survey
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SurveyRepo {

    val dbRefSurvey = FirebaseDatabase.getInstance()
        .getReference("posts")
        .child("survey")

    val surveyList: MutableLiveData<List<Post>> = MutableLiveData()


   /* fun getSurveyList(): MutableLiveData<List<Survey>> {
        return surveyList
    }*/

    suspend fun insertSurvey(survey: Post) : Boolean{
        return try {
            dbRefSurvey.push().setValue(survey)
            true
        }catch (e : Exception){
            false
        }
    }


    fun getSurvey(){
        dbRefSurvey
            .orderByChild("survey")
            .addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                    var surveyl = ArrayList<Post>()
                    for (s in snapshot.children){
                        val surveyModel = s.getValue(Post::class.java)!!
                        if (surveyModel.deleteState.equals("0")){
                            surveyModel.postID = s.key!!
                            surveyl.add(surveyModel)
                        }
                    }
                surveyList.value = surveyl
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}