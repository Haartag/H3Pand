package com.valerytimofeev.h3pand.di

import android.content.Context
import androidx.room.Room
import com.valerytimofeev.h3pand.data.local.PandDao
import com.valerytimofeev.h3pand.data.local.PandDatabase
import com.valerytimofeev.h3pand.repositories.local.DefaultPandRepository
import com.valerytimofeev.h3pand.repositories.local.PandRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesPandDatabase(@ApplicationContext appContext: Context): PandDatabase {
        return Room.databaseBuilder(
            appContext,
            PandDatabase::class.java,
            "PandDB"
        )
            .createFromAsset("database/database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providePandDao(pandDatabase: PandDatabase): PandDao {
        return pandDatabase.pandDao()
    }

    @Singleton
    @Provides
    fun provideDefaultStorageRepository(
        dao: PandDao
    ) = DefaultPandRepository(dao) as PandRepository
}