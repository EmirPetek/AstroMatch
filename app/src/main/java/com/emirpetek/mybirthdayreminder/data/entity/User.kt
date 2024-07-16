package com.emirpetek.mybirthdayreminder.data.entity

import java.io.Serializable

data class User(
    val userID:String,
    val fullname:String,
    val email:String,
    val password:String,
    val birthdate:String,
    val birthTime:String,
    val profile_img:String,
    val created_at:Long,
    val accountDeleteState:String,
    val accountDeleteTime:Long,
    val zodiac:String,
    val ascendant:String
) : Serializable{

    constructor(): this("","","","","","","",0,"",0,"","")

}
