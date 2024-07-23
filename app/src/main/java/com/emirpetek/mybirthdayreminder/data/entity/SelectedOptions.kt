package com.emirpetek.mybirthdayreminder.data.entity

import java.io.Serializable

data class SelectedOptions(
    val userID:String,
    val postID:String,
    val selectedOption:Int,
    val timestamp:Long
): Serializable{


    constructor() : this("","",0,0)
}
