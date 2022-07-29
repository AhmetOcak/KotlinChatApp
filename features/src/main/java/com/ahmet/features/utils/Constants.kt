package com.ahmet.features.utils

object Constants {
    const val EMPTY_FIELD_MESSAGE = "Please fill the all empty fields"
    const val EMAIL_MESSAGE = "Please enter a valid e-mail address"
    const val PASSWORD_LENGTH_MESSAGE = "Password length cannot be less than 5"
    const val PASSWORD_MATCH_MESSAGE = "Password does not match"
    const val IS_COME_FROM_APP = "isComeFromApp"

    const val USER_EMAIL_ARG_NAME = "userName"

    const val USER_FRIEND_ARG_KEY = "friend_email"
    const val USER_FRIEND_NAME_ARG_KEY = "friend_user_name"
}

object FirebaseCommonMessages {
    const val NETWORK_ERROR =
        "A network error (such as timeout, interrupted connection or unreachable host) has occurred."
    const val UNKNOWN_ERROR = "Something went wrong, please try again later"
}

object FirebaseRegisterMessages {
    const val EMAIL_ERROR = "The email address is already in use by another account."
    const val SUCCESSFUL = "Register Successful"
}

object FirebaseLoginMessages {
    const val SUCCESSFUL = "Login Successful"
    const val LOGIN_ERROR = "Login Failed: Your user e-mail or password is incorrect"
    const val LOGIN_PASSWORD_ERROR = "The password is invalid or the user does not have a password."
    const val BLOCKED_ERROR_MESSAGE =
        "We have blocked all requests from this device due to unusual activity. Try again later. " +
                "[ Access to this account has been temporarily disabled due to many failed login attempts. " +
                "You can immediately restore it by resetting your password or you can try again later. ]"

    const val ERROR_MESSAGE =
        "There is no user record corresponding to this identifier. The user may have been deleted."
}

object FirebaseDeleteMessages {
    const val WRONG_PASSWORD = "The password is invalid or the user does not have a password."
    const val DEL_SUCCESSFUL = "Your account deleted successfully"
}

object CollectionPaths {
    const val USER = "users"
}
