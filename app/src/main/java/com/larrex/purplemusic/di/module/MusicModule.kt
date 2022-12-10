package com.larrex.purplemusic.di.module

import com.larrex.purplemusic.di.repository.RepositoryImpl
import com.larrex.purplemusic.domain.repository.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MusicModule {

    @Singleton
    @Binds
    abstract fun provideRepository(repositoryImpl: RepositoryImpl): Repository

}