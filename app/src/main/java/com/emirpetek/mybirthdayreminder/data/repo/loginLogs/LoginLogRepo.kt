package com.emirpetek.mybirthdayreminder.data.repo.loginLogs

import com.emirpetek.mybirthdayreminder.data.entity.loginLogs.LogDetails
import com.emirpetek.mybirthdayreminder.data.entity.loginLogs.UserLoginLogs
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginLogRepo {

    val logDataRepo = Firebase.firestore
        .collection("loginLogs")
        .document("logs")
        .collection(Firebase.auth.currentUser!!.uid)
        .document("logData")

    val logDataListRepo = Firebase.firestore
        .collection("loginLogs")
        .document("logs")
        .collection(Firebase.auth.currentUser!!.uid)
        .document("logDataList")
        .collection("logDataList")

    fun setLoginLog(log: UserLoginLogs){
        val map = mapOf(
            "lastLoginTime" to System.currentTimeMillis(),
            "userID" to log.userID
        )
        logDataRepo.set(log)
    }

    fun addLogDataToList(logLong: LogDetails){
        logDataListRepo.add(logLong)
    }

}