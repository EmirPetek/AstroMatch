package com.emirpetek.mybirthdayreminder.data.entity

import java.io.Serializable

data class UserCredits(
    val userID:String,
    val amount: Long
): Serializable{

    constructor() : this("",0)
}
