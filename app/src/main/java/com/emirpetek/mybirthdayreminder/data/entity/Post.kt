package com.emirpetek.mybirthdayreminder.data.entity

import java.io.Serializable

data class Post(
    var question: ArrayList<Question>? = null,
    var survey: ArrayList<Survey>? = null
):Serializable{

}
