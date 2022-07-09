package com.ahmet.core.utils

object EmailController {

    fun emailController(email: String): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}