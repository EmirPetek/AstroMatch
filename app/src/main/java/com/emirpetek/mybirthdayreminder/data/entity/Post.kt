package com.emirpetek.mybirthdayreminder.data.entity

import android.os.Parcel
import android.os.Parcelable
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
    var selectedOptions:ArrayList<SelectedOptions>? = null,
    var userFullname:String?= null,
    var userImg:String?=null
    ): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        TODO("imageURL"),
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readLong(),
        TODO("options"),
        TODO("questionAnswers"),
        TODO("selectedOptions"),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    constructor() : this("","","","", arrayListOf(),0,"",0, arrayListOf(), arrayListOf(),
            arrayListOf(),"")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(postID)
        parcel.writeString(userID)
        parcel.writeString(postType)
        parcel.writeString(questionText)
        parcel.writeLong(timestamp)
        parcel.writeString(deleteState)
        parcel.writeLong(deleteTimestamp)
        parcel.writeString(userFullname)
        parcel.writeString(userImg)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }
}
