package com.emirpetek.mybirthdayreminder.data.entity.like

import com.emirpetek.mybirthdayreminder.data.enum.LikeType
import java.io.Serializable

data class Like(
    val senderUserId: String,
    val receiverUserId: String,
    val timestamp: Long,
    val type: LikeType,
    val likeID:String,
    val deleteState: Int? = null,
    val deleteTime: Long? = null
) : Serializable{
    constructor() : this("","",0,LikeType.NORMAL,"",0,0)
}
