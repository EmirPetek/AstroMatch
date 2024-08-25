package com.emirpetek.mybirthdayreminder.data.entity.like

import com.emirpetek.mybirthdayreminder.data.entity.user.User
import com.emirpetek.mybirthdayreminder.data.enum.LikeType
import java.io.Serializable

data class Like(
    val senderUserId: String,
    val receiverUserId: String,
    val timestamp: Long,
    val type: LikeType,
    var likeID:String,
    val deleteState: Int? = null,
    val deleteTime: Long? = null,
    var user: User? = null
) : Serializable{
    constructor() : this("","",0,LikeType.NORMAL,"",0,0)
}
