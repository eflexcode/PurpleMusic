package com.larrex.purplemusic.di.module

import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.larrex.purplemusic.di.repository.RepositoryImpl
import com.larrex.purplemusic.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MusicModule {

    @Singleton
    @Provides
    fun provideRepository(repositoryImpl: RepositoryImpl): Repository {

        return repositoryImpl

    }

   @Singleton
    @Provides
    fun providesAtt(): AudioAttributes {
        return AudioAttributes.Builder()
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .setUsage(C.USAGE_MEDIA)
            .build()
    }

    @Singleton
    @Provides
    fun providesExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes
    ): ExoPlayer {

        return ExoPlayer.Builder(context)
            .setAudioAttributes(audioAttributes, true)
            .setHandleAudioBecomingNoisy(true)
            .build()
    }



//    @Singleton
//    @Provides
//    fun providesDataSource(@ApplicationContext context: Context): DefaultDataSourceFactory {
//        return DefaultDataSourceFactory(
//            context,
//            Util.getUserAgent(context, "Purple Music")
//        )
//    }
}