package com.emirpetek.mybirthdayreminder.data.entity.user

data class ProfileView(
    val viewID:String,
    val visitorID:String,
    val viewedID:String,
    val timestamp: Long = System.currentTimeMillis()
)
