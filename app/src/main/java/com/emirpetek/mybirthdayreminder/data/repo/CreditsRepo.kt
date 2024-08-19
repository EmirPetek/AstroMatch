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

    val credits: MutableLiveData<UserCredits> = MutableLiveData()

    fun getCreditAmount() : MutableLiveData<UserCredits>{
        return credits
    }


    fun createCreditWithRegister(amount: Long){
        creditsRef.set(UserCredits(userID,amount,30,10,System.currentTimeMillis()))
    }

    fun getUserCreditsAmountFun(){
        creditsRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Hata işleme
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val credit = snapshot.toObject(UserCredits::class.java)!!
                credits.value = credit
            }
        }
    }

    fun incrementUserCredit(amount: Long){
        creditsRef.update("amount",FieldValue.increment(amount))
    }

    fun decrementUserCredit(amount: Long){
        creditsRef.update("amount",FieldValue.increment(-1 * amount))
    }

    fun incrementLikeRight(amount: Long){
        creditsRef.update("likeRights",FieldValue.increment(amount))
    }

    fun decrementLikeRight(amount: Long){
        creditsRef.update("likeRights",FieldValue.increment(-1 * amount))
    }

    fun incrementMegaLikeRight(amount: Long){
        creditsRef.update("megaLikeRights",FieldValue.increment(amount))
    }

    fun decrementMegaLikeRight(amount: Long){
        creditsRef.update("megaLikeRights",FieldValue.increment(-1 * amount))
    }

    fun setDailyBonusValues(){
        creditsRef.update("likeRights",30)
        creditsRef.update("megaLikeRights",10)
        creditsRef.update("lastCreditBalanceTimestamp",System.currentTimeMillis())
        creditsRef.update("amount",FieldValue.increment(20))
    }




}