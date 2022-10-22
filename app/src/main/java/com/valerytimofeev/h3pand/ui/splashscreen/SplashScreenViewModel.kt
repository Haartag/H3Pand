package com.valerytimofeev.h3pand.ui.splashscreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valerytimofeev.h3pand.domain.model.CurrentLocal
import com.valerytimofeev.h3pand.domain.model.SettingsDataStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    settingsDataStorage: SettingsDataStorage,
): ViewModel() {

    val dataLoaded = mutableStateOf(false)

    init {
        viewModelScope.launch {
            //Place current localization into singleton object
            CurrentLocal.local = settingsDataStorage.getLanguage.first() ?: 0
            delay(1000)
            dataLoaded.value = true
        }
    }
}