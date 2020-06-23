package com.hms.features.utils

import java.text.SimpleDateFormat
import java.util.*

class Utils {

    companion object{
        fun getTime(): String
        {
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())

            return currentDate.toString()
        }

    }




}