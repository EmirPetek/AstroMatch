package com.emirpetek.mybirthdayreminder.data.entity.birthdays

import java.io.Serializable

data class BirthdayGiftIdea(
    val saverID:String,
    val userDegree: Int,
    val giftIdea: String,
    val birthdayKey:String,
    val timestamp: Long = System.currentTimeMillis()
): Serializable {
    constructor() : this("",0,"","")
}