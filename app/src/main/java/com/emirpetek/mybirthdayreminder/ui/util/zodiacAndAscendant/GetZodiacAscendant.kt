package com.emirpetek.mybirthdayreminder.ui.util.zodiacAndAscendant

import android.content.Context
import com.emirpetek.mybirthdayreminder.R

class GetZodiacAscendant(
    val mContext: Context,
    val index: Int,
) {

    fun getZodiacOrAscendantSignByIndex(): String {
        return when (index) {
            1 -> mContext.getString(R.string.aries)       // Koç
            2 -> mContext.getString(R.string.taurus)      // Boğa
            3 -> mContext.getString(R.string.gemini)      // İkizler
            4 -> mContext.getString(R.string.cancer)      // Yengeç
            5 -> mContext.getString(R.string.leo)         // Aslan
            6 -> mContext.getString(R.string.virgo)       // Başak
            7 -> mContext.getString(R.string.libra)       // Terazi
            8 -> mContext.getString(R.string.scorpio)     // Akrep
            9 -> mContext.getString(R.string.sagittarius) // Yay
            10 -> mContext.getString(R.string.capricorn)  // Oğlak
            11 -> mContext.getString(R.string.aquarius)   // Kova
            12 -> mContext.getString(R.string.pisces)     // Balık
            else -> mContext.getString(R.string.unknown)  // Bilinmeyen
        }
    }

}