package com.emirpetek.mybirthdayreminder.data.entity

import com.google.firebase.Timestamp
import java.io.Serializable

data class Question(
    val questionID:String,
    val userID:String,
    val questionText:String,
    val imageURL:ArrayList<String>,
    val timestamp: Long,
    val deleteState: String,
    val deleteTimestamp: Long
): Serializable{
        constructor() : this("","","", arrayListOf(),0,"",0)
}
