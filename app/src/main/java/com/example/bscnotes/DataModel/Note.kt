package com.example.bscnotes.DataModel

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.*

data class Note(val id: String?, val noteText: String? = null, val date: Date? = null) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        date = Date(parcel.readLong())
    )

    @SuppressLint("SimpleDateFormat")
    fun getStringDate(): String{
        val format = SimpleDateFormat("dd/MM/yyy")
        return format.format(date!!)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(noteText)
        if (date != null) {
            parcel.writeLong(date.time)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}