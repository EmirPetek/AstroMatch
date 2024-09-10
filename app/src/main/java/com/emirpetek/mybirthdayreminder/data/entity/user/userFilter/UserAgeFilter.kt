package com.emirpetek.mybirthdayreminder.data.entity.user.userFilter

data class UserAgeFilter(var min: Float, var max:Float){
    constructor() : this(0.0.toFloat(), 0.0.toFloat())
}
