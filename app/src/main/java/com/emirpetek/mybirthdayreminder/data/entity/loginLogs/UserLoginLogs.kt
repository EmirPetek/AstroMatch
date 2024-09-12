package com.emirpetek.mybirthdayreminder.data.entity.loginLogs

import java.io.Serializable

data class UserLoginLogs(
    val logID:String,
    var userID: String,
    val lastLoginTime: Long,
) : Serializable{
    constructor() : this("","",0)
}
