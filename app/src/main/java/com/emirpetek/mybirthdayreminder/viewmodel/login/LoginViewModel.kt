package com.emirpetek.mybirthdayreminder.viewmodel.login

import androidx.lifecycle.ViewModel
import com.emirpetek.mybirthdayreminder.data.entity.loginLogs.LogDetails
import com.emirpetek.mybirthdayreminder.data.entity.loginLogs.UserLoginLogs
import com.emirpetek.mybirthdayreminder.data.repo.loginLogs.LoginLogRepo

class LoginViewModel : ViewModel() {

    val logRepo = LoginLogRepo()

    fun setLoginLog(log: UserLoginLogs){
        logRepo.setLoginLog(log)
    }

    fun addLogDataToList(logLong: LogDetails){
        logRepo.addLogDataToList(logLong)
    }


    }