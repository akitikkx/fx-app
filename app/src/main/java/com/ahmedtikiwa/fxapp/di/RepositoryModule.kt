package com.ahmedtikiwa.fxapp.di

import com.ahmedtikiwa.fxapp.database.FXAppDao
import com.ahmedtikiwa.fxapp.repository.FXAppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideFXAppRepository(fxAppDao: FXAppDao): FXAppRepository {
        return FXAppRepository(fxAppDao)
    }
}