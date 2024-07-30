package com.emirpetek.mybirthdayreminder.data.entity.question

import java.io.Serializable

data class QuestionAnswers(
    val userID:String,
    val postID:String,
    val answer:String,
    val timestamp:Long,
    var userFullname:String? = null,
): Serializable {

    constructor() : this("","","",0,"")

}
