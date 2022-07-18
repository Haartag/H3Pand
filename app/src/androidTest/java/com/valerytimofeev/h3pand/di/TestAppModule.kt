package com.valerytimofeev.h3pand.di

import android.content.Context
import androidx.room.Room
import com.valerytimofeev.h3pand.data.local.PandDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
class TestAppModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDb(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(appContext, PandDatabase::class.java, "testPandDb")
            .allowMainThreadQueries()
            .createFromAsset("database/database.db")
            .build()
}