package com.ahmet.features.utils

object Constants {
    const val EMPTY_FIELD_MESSAGE = "Please fill the all empty fields"
    const val EMAIL_MESSAGE = "Please enter a valid e-mail address"
    const val PASSWORD_LENGTH_MESSAGE = "Password length cannot be less than 5"
    const val PASSWORD_MATCH_MESSAGE = "Password does not match"
}

object FirebaseMessages {
    const val EMAIL_ERROR = "The email address is already in use by another account."
    const val NETWORK_ERROR =
        "A network error (such as timeout, interrupted connection or unreachable host) has occurred."
    const val SUCCESSFUL = "Register Successful"
}

object CollectionPaths {
    const val USER = "users"
}