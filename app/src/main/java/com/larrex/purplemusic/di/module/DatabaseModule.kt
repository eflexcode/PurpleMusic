package com.larrex.purplemusic.di.module

import android.app.Application
import androidx.room.Room
import com.larrex.purplemusic.di.repository.RepositoryImpl
import com.larrex.purplemusic.domain.repository.Repository
import com.larrex.purplemusic.domain.room.nowplayingroom.NowPlayingAndNextUpsDatabase
import dagger.Binds
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
        fun provideNowPlayDatabase(context : Application): NowPlayingAndNextUpsDatabase {

            return Room.databaseBuilder(
                context,
                NowPlayingAndNextUpsDatabase::class.java,
                "NowPlayingAndNextUpsDatabase"
            ).build()

        }

    }

}