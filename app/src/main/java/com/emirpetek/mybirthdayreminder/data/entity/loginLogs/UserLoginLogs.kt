package com.emirpetek.mybirthdayreminder.data.entity.loginLogs

import java.io.Serializable

data class UserLoginLogs(
    val logID:String,
    val userID: String,
    val lastLoginTime: Long,
) : Serializable{
    constructor() : this("","",0)
}
