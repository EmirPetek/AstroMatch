package com.emirpetek.mybirthdayreminder.data.entity.user

import java.io.Serializable

data class ProfileVisit(
    val viewID:String,
    val visitorID:String,
    val viewedID:String,
    val timestamp: Long = System.currentTimeMillis(),
    var user : User? = null
) : Serializable {
    constructor() : this("","","")
}
