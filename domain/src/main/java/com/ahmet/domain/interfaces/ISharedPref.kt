package com.ahmet.domain.interfaces

import com.ahmet.domain.model.User

interface ISharedPref {

    fun saveUserData(user: User)

    fun getUserData(): User
}