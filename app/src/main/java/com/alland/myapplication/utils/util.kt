package com.alland.myapplication.utils

import java.text.SimpleDateFormat

object util {
    fun convertDateStringToFormattedString(dateString: String?): String? {

        if(dateString != null){
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                val date = dateString.let { inputFormat.parse(it) }

                val outputFormat = SimpleDateFormat("dd/MM/yyyy")


                val result = outputFormat.format(date)

                return outputFormat.format(date)
            }catch (e: Exception){
                return "-"
            }
        }

        return null
    }
}