package com.example.bscnotes

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

data class Note(val noteText: String, val date: Date) {

    @SuppressLint("SimpleDateFormat")
    fun getStringDate(): String{
        val format = SimpleDateFormat("dd/MM/yyy")
        return format.format(date)
    }
}