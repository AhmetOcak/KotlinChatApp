package com.ahmet.data.di.modules

import com.ahmet.data.repository.FirebaseUserDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseUserDataRepositoryModule {

    @Singleton
    @Provides
    fun provideFirebaseUserDataRepository(): FirebaseUserDataRepository {
        return FirebaseUserDataRepository()
    }
}