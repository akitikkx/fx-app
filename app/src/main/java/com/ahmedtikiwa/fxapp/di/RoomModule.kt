package com.ahmedtikiwa.fxapp.di

import android.content.Context
import androidx.room.Room
import com.ahmedtikiwa.fxapp.database.FXAppDao
import com.ahmedtikiwa.fxapp.database.FXAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RoomModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): FXAppDatabase {
        return Room.databaseBuilder(context, FXAppDatabase::class.java, "fxapp")
            .build()
    }

    @Singleton
    @Provides
    fun provideFXAppDao(fxAppDatabase: FXAppDatabase): FXAppDao {
        return fxAppDatabase.fxAppDao
    }
}