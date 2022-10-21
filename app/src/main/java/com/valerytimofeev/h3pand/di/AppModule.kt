package com.valerytimofeev.h3pand.di

import android.content.Context
import androidx.room.Room
import com.valerytimofeev.h3pand.data.local.PandDao
import com.valerytimofeev.h3pand.data.local.PandDatabase
import com.valerytimofeev.h3pand.domain.model.SettingsDataStorage
import com.valerytimofeev.h3pand.domain.use_case.*
import com.valerytimofeev.h3pand.domain.use_case.dialog_use_case.ConvertToSearchListUseCase
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


    /**
     * Use case DI
     */

    @Singleton
    @Provides
    fun provideGetGuardCharacteristicsUseCase(): GetGuardCharacteristicsUseCase {
        return GetGuardCharacteristicsUseCase()
    }

    @Singleton
    @Provides
    fun provideGetBoxWithPercentUseCase(): GetBoxWithPercentUseCase {
        return GetBoxWithPercentUseCase()
    }

    @Singleton
    @Provides
    fun provideGetDwellingValueUseCase(): GetDwellingValueUseCase {
        return GetDwellingValueUseCase()
    }

    @Singleton
    @Provides
    fun provideConvertToSearchListUseCase(): ConvertToSearchListUseCase {
        return ConvertToSearchListUseCase()
    }

    @Singleton
    @Provides
    fun provideSettingsDataStorage(@ApplicationContext appContext: Context): SettingsDataStorage {
        return SettingsDataStorage(appContext)
    }

    @Singleton
    @Provides
    fun provideGetLocalizedTextUseCase(): GetLocalizedTextUseCase {
        return GetLocalizedTextUseCase()
    }
}