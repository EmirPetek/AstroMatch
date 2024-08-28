package com.emirpetek.mybirthdayreminder.ui.util.userDegree

import android.content.Context
import com.emirpetek.mybirthdayreminder.R

class GetUserDegree(val mContext: Context) {


    fun getUserDegree(degree: Int): String {
        return when (degree) {
            1 -> mContext.getString(R.string.family)
            2 -> mContext.getString(R.string.friends)
            3 -> mContext.getString(R.string.bros)
            4-> mContext.getString(R.string.colleagues)
            5 -> mContext.getString(R.string.acquaintances)
            6 -> mContext.getString(R.string.partner)
            7 -> mContext.getString(R.string.vip)
            else -> mContext.getString(R.string.unknown) // Varsayılan değer için
        }
    }

}