package com.emirpetek.mybirthdayreminder.data.entity

import java.io.Serializable

data class Survey(
    val surveyID: String,
    val userID:String,
    val questionText: String,
    val options:ArrayList<String>,
    val imageURL:ArrayList<String>,
    val timestamp:Long,
    val deleteState:String,
    val deleteTime:Long
) : Serializable{
        constructor() : this("","","", arrayListOf("0","1"),arrayListOf("0","1"),0,"",0)
}
