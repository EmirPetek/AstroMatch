package com.emirpetek.mybirthdayreminder.data.entity

import java.io.Serializable

data class Post(
    val question: ArrayList<Question>,
    val survey: ArrayList<Survey>
):Serializable{

}
