package com.example.bscnotes.DataModel

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

data class Note(val id: String?, val noteText: String? = null, val date: Date? = null) {

    @SuppressLint("SimpleDateFormat")
    fun getStringDate(): String{
        val format = SimpleDateFormat("dd/MM/yyy")
        return format.format(date!!)
    }
}