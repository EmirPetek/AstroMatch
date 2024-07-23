package com.emirpetek.mybirthdayreminder.data.entity

import java.io.Serializable

class Post(
    var postID:String,
    val userID:String,
    val postType:String,
    val questionText:String,
    val imageURL:ArrayList<String>,
    val timestamp: Long,
    val deleteState: String,
    val deleteTimestamp: Long,
    val options:ArrayList<String>? = null,
    val questionAnswers:ArrayList<QuestionAnswers>? = null,
    var selectedOptions:ArrayList<SelectedOptions>? = null
    ):Serializable{

        constructor() : this("","","","", arrayListOf(),0,"",0, arrayListOf(), arrayListOf())
}
