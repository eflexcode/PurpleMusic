package com.larrex.purplemusic.di.module

import android.content.Context
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes

import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ExoServiceModule {

//    @ServiceScoped
//    @Provides
//    fun providesAtt() = AudioAttributes.Builder()
//        .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
//        .setUsage(C.USAGE_MEDIA)
//        .build()
//
//    @ServiceScoped
//    @Provides
//    fun providesExoPlayer(@ApplicationContext context: Context,audioAttributes: AudioAttributes)  = SimpleExoPlayer
//        .Builder(context).apply {
//            setAudioAttributes(audioAttributes,true)
//            setHandleAudioBecomingNoisy(true)
//
//        }
//        .build()
//
//    @ServiceScoped
//    @Provides
//    fun providesDataSource(@ApplicationContext context: Context) = DefaultDataSourceFactory(context,Util.getUserAgent(context,"Purple Music"))
}