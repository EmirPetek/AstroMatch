package com.emirpetek.mybirthdayreminder.data.repo

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
        creditsRef.get()
            .addOnSuccessListener { node ->
                val credit = node.toObject(UserCredits::class.java)!!
                credits.value = credit.amount
            }
    }




}