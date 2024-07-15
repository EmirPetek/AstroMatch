package com.emirpetek.mybirthdayreminder.data.entity

import java.io.Serializable

data class User(
    val userID:String,
    val fullname:String,
    val email:String,
    val password:String,
    val birthdate:String,
    val profile_img:String,
    val created_at:Long
) : Serializable{

}
