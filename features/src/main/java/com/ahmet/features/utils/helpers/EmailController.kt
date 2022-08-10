package com.ahmet.features.utils.helpers

object EmailController {

    fun emailController(email: String): Boolean =
        android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}