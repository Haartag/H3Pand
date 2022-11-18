package com.valerytimofeev.h3pand.domain.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Preferences data store for settings
 */
class SettingsDataStorage(private val context: Context ) {

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        val LANGUAGE = intPreferencesKey("language")
        val LIST_TYPE = booleanPreferencesKey("listType")
        val MIN_PERCENT = floatPreferencesKey("minPercent")
    }

    val getLanguage: Flow<Int?> = context.dataStore.data.map { preferences ->
        preferences[LANGUAGE] ?: 0
    }

    val getListType: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[LIST_TYPE] ?: true
    }

    val getMinPercent: Flow<Float> = context.dataStore.data.map { preferences ->
        preferences[MIN_PERCENT] ?: 0.05f
    }

    suspend fun setLanguage(language: Int) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE] = language
        }
    }

    suspend fun setListType(type: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[LIST_TYPE] = type
        }
    }

    suspend fun setMinPercent(minPercent: Float) {
        context.dataStore.edit { preferences ->
            preferences[MIN_PERCENT] = minPercent
        }
    }
}