package com.ryanconway.spaceflightnews.di

import android.content.Context
import com.ryanconway.spaceflightnews.domain.repository.ConnectivityRepository
import com.ryanconway.spaceflightnews.data.repository.ConnectivityRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ConnectivityModule {

    @Provides
    fun provideConnectivityModule(@ApplicationContext context: Context): ConnectivityRepository =
        ConnectivityRepositoryImpl(context)
}