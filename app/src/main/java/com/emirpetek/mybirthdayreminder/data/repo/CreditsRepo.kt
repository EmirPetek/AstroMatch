package com.emirpetek.mybirthdayreminder.data.repo

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.UserCredits
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreditsRepo {

    val userID = Firebase.auth.currentUser!!.uid
    val creditsRef = Firebase.firestore
        .collection("credits")
        .document("userCredits")
        .collection(userID)
        .document(userID)

    val credits: MutableLiveData<Long> = MutableLiveData()

    fun getCreditAmount() : MutableLiveData<Long>{
        return credits
    }


    fun createCreditWithRegister(amount: Long){
        creditsRef.set(UserCredits(userID,amount))
    }

    fun getUserCreditsAmountFun(){
        creditsRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Hata işleme
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val credit = snapshot.toObject(UserCredits::class.java)!!
                credits.value = credit.amount
            }
        }
    }

    fun incrementUserCredit(amount: Long){
        creditsRef.update("amount",FieldValue.increment(amount))
    }

    fun decrementUserCredit(amount: Long){
        creditsRef.update("amount",FieldValue.increment(-amount))
    }




}