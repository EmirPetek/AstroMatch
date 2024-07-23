package com.emirpetek.mybirthdayreminder.data.repo.social

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.Post
import com.emirpetek.mybirthdayreminder.data.entity.SelectedOptions
import com.emirpetek.mybirthdayreminder.data.entity.Survey
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SurveyRepo {

    val dbRefSurvey = FirebaseDatabase.getInstance()
        .getReference("posts")
        .child("survey")

    val surveyOptionsRef = FirebaseDatabase.getInstance()
        .getReference("posts")
        .child("surveyOptions")


    val surveyList: MutableLiveData<List<Post>> = MutableLiveData()
    val surveyOption: MutableLiveData<List<SelectedOptions>> = MutableLiveData()

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
            .addListenerForSingleValueEvent(object : ValueEventListener{
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

    fun checkSelectedOptions(selectedOptions: SelectedOptions) {
        val ref = surveyOptionsRef.child(selectedOptions.postID)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var selectedList : ArrayList<SelectedOptions> = arrayListOf()
                if (snapshot.exists()) {
                    for(i in snapshot.children){
                       // Log.e("laskş","survey repo veri var")
                        val obj = i.getValue(SelectedOptions::class.java)!!
                        selectedList.add(obj)


                    }
                } else {
                }

                surveyOption.value = selectedList
            }

            override fun onCancelled(error: DatabaseError) {
                // Veritabanı hatası
                Log.e("Firebase", "Veritabanı hatası: ${error.message}")
            }
        })
    }

    // Yeni selectedOptions'ı ekleyen fonksiyon
    fun insertSelectedOption(selectedOptions: SelectedOptions) {
        val ref = surveyOptionsRef.child(selectedOptions.postID)
        ref.push().setValue(selectedOptions)
    }

   /* suspend fun insertSelectedSurvey(selectedOptions: SelectedOptions) : Boolean {

        return try {
            val newRef = FirebaseDatabase
                .getInstance()
                .getReference("posts")
                .child("surveyOptions")
                .child(selectedOptions.postID)
            // userID'yi kontrol et
            newRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // userID mevcut, ilgili node'daki değeri getir
                        val existingData = snapshot.getValue(SelectedOptions::class.java)
                        if (existingData != null) {
                            Log.d("Firebase", "Mevcut veri: $existingData")
                        } else {
                            Log.e("Firebase", "Mevcut veri getirilemedi.")
                        }
                        surveyOption.value = existingData

                    } else {
                        // userID mevcut değil, yeni veriyi kaydet
                        newRef.setValue(selectedOptions)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Veri başarıyla kaydedildi
                                    Log.d("Firebase", "Veri başarıyla kaydedildi.")
                                } else {
                                    // Veri kaydetme sırasında hata oluştu
                                    Log.e(
                                        "Firebase",
                                        "Veri kaydedilemedi: ${task.exception?.message}"
                                    )
                                }
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Veritabanı hatası
                    Log.e("Firebase", "Veritabanı hatası: ${error.message}")
                }
            })
            true
        }catch (e : Exception){
            false
        }

    }*/

}