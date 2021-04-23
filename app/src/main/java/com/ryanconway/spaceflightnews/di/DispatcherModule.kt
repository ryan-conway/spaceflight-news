package com.ryanconway.spaceflightnews.di

import com.ryanconway.spaceflightnews.domain.repository.DispatcherRepository
import com.ryanconway.spaceflightnews.data.repository.DispatcherRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DispatcherModule {

    @Provides
    fun providerDispatcherRepository(): DispatcherRepository = DispatcherRepositoryImpl()
}