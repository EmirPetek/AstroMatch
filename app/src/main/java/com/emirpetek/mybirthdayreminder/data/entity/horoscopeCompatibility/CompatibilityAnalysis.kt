package com.emirpetek.mybirthdayreminder.data.entity.horoscopeCompatibility

import java.io.Serializable

data class CompatibilityAnalysis(
    val id:String,
    val timestamp: Long = System.currentTimeMillis(),
    var compatibilityDescription: String? = null,
    val senderID: String,
    val firstUsername:String,
    val firstUserBirthdate:String,
    val secondUsername:String,
    val secondUserBirthdate:String,
    val analyseType:String
) : Serializable{
    constructor() : this("",0,null,"","","","","","")
}
