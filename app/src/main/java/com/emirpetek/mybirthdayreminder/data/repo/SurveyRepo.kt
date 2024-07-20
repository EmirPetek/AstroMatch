package com.emirpetek.mybirthdayreminder.data.repo

import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.Birthdays
import com.emirpetek.mybirthdayreminder.data.entity.Survey
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SurveyRepo {

    val dbRefSurvey = FirebaseDatabase.getInstance()
        .getReference("posts")
        .child("surveys")
        .child("survey")

    val surveyList: MutableLiveData<List<Survey>> = MutableLiveData()


   /* fun getSurveyList(): MutableLiveData<List<Survey>> {
        return surveyList
    }*/

    suspend fun insertSurvey(survey: Survey) : Boolean{
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
                    var surveyl = ArrayList<Survey>()
                    for (s in snapshot.children){
                        val surveyModel = s.getValue(Survey::class.java)!!
                        if (surveyModel.deleteState.equals("0")){
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