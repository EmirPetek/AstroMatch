package com.emirpetek.mybirthdayreminder.data.entity.loginLogs

import java.io.Serializable

data class LogDetails(
    val timestamp: Long,
    val ipAddress:String? = null
): Serializable {
    constructor() : this(0,"")
}
