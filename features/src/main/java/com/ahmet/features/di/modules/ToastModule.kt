package com.ahmet.features.di.modules

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ToastModule {

    @Singleton
    @Provides
    fun provideToast(@ApplicationContext context: Context): Toast {
        val toast = Toast.makeText(context, " ", Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 0)
        return toast
    }
}