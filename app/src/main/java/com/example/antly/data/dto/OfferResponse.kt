package com.example.antly.data.dto

import android.os.Parcel
import android.os.Parcelable
import kotlin.String

data class OfferResponse(
    var id: Int,
    var title: String,
    var descriptionShort: String,
    var descriptionLong: String,
    var subject: String,
    var location: String,
    var imageUrl: String,
    var price: Int,
    var teacherName: String,
    var range: String
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(descriptionShort)
        parcel.writeString(descriptionLong)
        parcel.writeString(subject)
        parcel.writeString(location)
        parcel.writeString(imageUrl)
        parcel.writeInt(price)
        parcel.writeString(teacherName)
        parcel.writeString(range)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OfferResponse> {
        override fun createFromParcel(parcel: Parcel): OfferResponse {
            return OfferResponse(parcel)
        }

        override fun newArray(size: Int): Array<OfferResponse?> {
            return arrayOfNulls(size)
        }
    }
}
