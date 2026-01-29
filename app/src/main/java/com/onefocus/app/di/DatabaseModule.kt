package com.onefocus.app.di

import android.content.Context
import androidx.room.Room
import com.onefocus.app.data.local.AppDatabase
import com.onefocus.app.data.local.dao.*
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
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "onefocus_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideHabitDao(database: AppDatabase): HabitDao {
        return database.habitDao()
    }

    @Provides
    @Singleton
    fun provideJourneyDao(database: AppDatabase): JourneyDao {
        return database.journeyDao()
    }

    @Provides
    @Singleton
    fun provideMoodDao(database: AppDatabase): MoodDao {
        return database.moodDao()
    }

    @Provides
    @Singleton
    fun provideRepeatingLogDao(database: AppDatabase): RepeatingLogDao {
        return database.repeatingLogDao()
    }
}
