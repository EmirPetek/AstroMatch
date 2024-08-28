package com.emirpetek.mybirthdayreminder.data.repo.birthdays

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.birthdays.BirthdayGiftIdea
import com.emirpetek.mybirthdayreminder.data.entity.birthdays.Birthdays
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class BirthdaysDaoRepo {

    val birthdayList: MutableLiveData<List<Birthdays>> = MutableLiveData()
    val giftIdeaList = MutableLiveData<List<BirthdayGiftIdea>>()


    private val dbFirestoreRef = Firebase.firestore.collection("birthdays")
    private val giftIdeaRef = Firebase.firestore.collection("birthdays").document("giftIdeas").collection("ideas")

    fun insertBirthdayFS(birthday: Birthdays) {
        dbFirestoreRef
            .document("savedBirthdays")
            .collection(birthday.saverID)
            .add(birthday)
            .addOnSuccessListener { documentReference ->

                val idea = BirthdayGiftIdea(birthday.saverID,birthday.userDegree,birthday.giftIdea,documentReference.id)
                insertBirthdayGiftIdea(idea)
                // Log.e("birthdaysdaorepo: ", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                // Log.e("birthdaysdaorepo", "Error adding document", e)
            }
    }

    fun getBirthdays(): MutableLiveData<List<Birthdays>> {
        return birthdayList
    }

    fun getBirthdaysData(userID: String) {
        dbFirestoreRef.document("savedBirthdays").collection(userID)
            .whereEqualTo("saverID", userID)
            .whereEqualTo("deletedState", "0")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("BirthdaysDaoRepo", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val bdList = ArrayList<Birthdays>()
                    for (document in snapshot.documents) {
                        val birthday = document.toObject(Birthdays::class.java)!!
                        birthday.birthdayKey = document.id
                        bdList.add(birthday)
                    }
                    birthdayList.value = bdList
                } else {
                    Log.d("BirthdaysDaoRepo", "No data found")
                    birthdayList.value = emptyList()
                }
            }
    }

    fun getSpecialBirthdayData(userID: String, birthdayKey: String) {
        dbFirestoreRef.document("savedBirthdays").collection(userID).document(birthdayKey)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("BirthdaysDaoRepo", "Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val bdList = ArrayList<Birthdays>()
                    val birthday = snapshot.toObject(Birthdays::class.java)!!
                    if (birthday.saverID == userID && birthday.deletedState == "0") {
                        birthday.birthdayKey = snapshot.id
                        bdList.add(birthday)
                    }
                    birthdayList.value = bdList
                } else {
                    Log.d("BirthdaysDaoRepo", "No data found")
                    birthdayList.value = emptyList()
                }
            }
    }

    fun updateBirthday(userID: String, birthdayKey: String, birthday: Map<String, Any>) {
        dbFirestoreRef.document("savedBirthdays").collection(userID).document(birthdayKey).update(birthday)
    }

    fun deleteBirthday(userID: String, birthdayKey: String, delete: Map<String, Any>) {
        dbFirestoreRef.document("savedBirthdays").collection(userID).document(birthdayKey).update(delete)
    }

    fun insertBirthdayGiftIdea(idea: BirthdayGiftIdea){
        val doc_id = "${System.currentTimeMillis()}_${idea.birthdayKey}"
        giftIdeaRef.document(doc_id).set(idea)
    }

    suspend fun getBirthdayGiftIdeas(){

        val list = giftIdeaRef.get().await().toObjects(BirthdayGiftIdea::class.java)
        giftIdeaList.value = list
    }
}
