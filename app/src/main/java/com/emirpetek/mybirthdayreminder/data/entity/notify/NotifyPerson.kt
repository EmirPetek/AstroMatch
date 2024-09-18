package com.emirpetek.mybirthdayreminder.data.entity.notify

import java.io.Serializable

data class NotifyPerson(
    val reportedUserID:String,
    val reporterUserID:String,
    val timestamp: Long,
    val notifyCategory: ArrayList<String>,
    val notifyAnotherMessage:String? = null,

) : Serializable {
    constructor() : this("","",0, arrayListOf())
}
