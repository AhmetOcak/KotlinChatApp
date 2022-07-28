package com.ahmet.data.utils

object EditEmail {
    fun removeDot(email: String): String = email.replace(".", "")

    fun addDot(email: String): String {
        val editedStr = email.substring(email.indexOf('@') + 1, email.indexOf('@') + 6) + ".com"
        val firstStr = email.substring(0, email.indexOf('@') + 1)
        return firstStr + editedStr
    }
}