package com.emirpetek.mybirthdayreminder.data.entity

data class ContactFeedback(
    val id:String,
    val feedback:String,
    val timestamp: Long,
    val userID:String
)
