package com.larrex.purplemusic.di.module

import android.app.Application
import androidx.room.Room
import com.larrex.purplemusic.domain.room.PurpleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    companion object{

        @Provides
        @Singleton
        fun provideNowPlayDatabase(context : Application): PurpleDatabase {

            return Room.databaseBuilder(
                context,
                PurpleDatabase::class.java,
                "NowPlayingAndNextUpsDatabase"
            ).build()

        }

    }

}