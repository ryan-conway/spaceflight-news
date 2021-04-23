package com.ryanconway.spaceflightnews.di

import android.content.Context
import androidx.room.Room
import com.ryanconway.spaceflightnews.data.cache.room.SpaceFlightDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SpaceFlightDatabase {
        return Room.databaseBuilder(
            context,
            SpaceFlightDatabase::class.java,
            "spaceflightnews.db"
        ).build()
    }
}