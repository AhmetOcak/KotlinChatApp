package com.chatapp

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ChatAppApplication : Application() {

    override fun getApplicationContext(): Context {
        return super.getApplicationContext()
    }
}