package com.emirpetek.mybirthdayreminder.data.repo.loginLogs

import androidx.lifecycle.MutableLiveData
import com.emirpetek.mybirthdayreminder.data.entity.loginLogs.LogDetails
import com.emirpetek.mybirthdayreminder.data.entity.loginLogs.UserLoginLogs
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginLogRepo {

    val logDataRepo = Firebase.firestore
        .collection("loginLogs")
        .document("logs")


    val logDataListRepo = Firebase.firestore
        .collection("loginLogs")
        .document("logs")



    private val loginLogLiveData : MutableLiveData<UserLoginLogs> = MutableLiveData()

    fun getLogLiveData() : MutableLiveData<UserLoginLogs>{
        return loginLogLiveData
    }

    fun getLoginLog(userID:String){
        Firebase.firestore
            .collection("loginLogs").document("logs")
            .collection(userID).document("logData")
            .get().addOnSuccessListener { it ->
                val logModel = it.toObject(UserLoginLogs::class.java)!!
                    logModel.userID = it.id
                    loginLogLiveData.value = logModel
            }
    }



    fun setLoginLog(log: UserLoginLogs){
        val map = mapOf(
            "lastLoginTime" to System.currentTimeMillis(),
            "userID" to log.userID
        )
        logDataRepo.collection(log.userID)
            .document("logData").set(log)
    }

    fun addLogDataToList(logLong: LogDetails){
        val docID = "${logLong.timestamp}_${logLong.userID}"
        logDataListRepo.collection(logLong.userID)
            .document("logDataList")
            .collection("logDataList").document(docID).set(logLong)
    }

}