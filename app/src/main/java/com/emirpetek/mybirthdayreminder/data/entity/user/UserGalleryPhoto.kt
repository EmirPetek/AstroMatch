package com.emirpetek.mybirthdayreminder.data.entity.user

import android.os.Parcel
import android.os.Parcelable

data class UserGalleryPhoto(
    val imageURL:String,
    val timestamp: Long
): Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readLong()
    ) {
    }

    constructor() : this("",0)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageURL)
        parcel.writeLong(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserGalleryPhoto> {
        override fun createFromParcel(parcel: Parcel): UserGalleryPhoto {
            return UserGalleryPhoto(parcel)
        }

        override fun newArray(size: Int): Array<UserGalleryPhoto?> {
            return arrayOfNulls(size)
        }
    }
}
