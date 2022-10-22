package com.valerytimofeev.h3pand.ui.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.valerytimofeev.h3pand.domain.model.CurrentLocal
import com.valerytimofeev.h3pand.domain.model.SettingsDataStorage
import com.valerytimofeev.h3pand.data.additional_data.TextStorage
import com.valerytimofeev.h3pand.domain.use_case.GetLocalizedTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsDataStorage: SettingsDataStorage,
    getLocalizedTextUseCase: GetLocalizedTextUseCase
) : ViewModel() {

    private val localeNumber = CurrentLocal.local
    private val newLocaleNumber = mutableStateOf(localeNumber)

    var languages = listOf(
        getLocalizedTextUseCase(TextStorage.SettingsLanguageEng.text),
        getLocalizedTextUseCase(TextStorage.SettingsLanguageRus.text)
    )

    val currentLanguage = mutableStateOf(languages[newLocaleNumber.value])

    val languageDropDownState = mutableStateOf(false)
    val snackBarVisible = mutableStateOf(false)

    val languageText = getLocalizedTextUseCase(TextStorage.SettingsLanguageHint.text)
    val sandboxText = getLocalizedTextUseCase(TextStorage.SettingsLanguageSandboxText.text)
    val sandboxButtonText = getLocalizedTextUseCase(TextStorage.SettingsLanguageSandboxButton.text)

    /**
     * Temporarily save selected language and, if the language is changed,
     * request that the application be restarted.
     */
    fun setLanguage(index: Int) {
        newLocaleNumber.value = index
        snackBarVisible.value = newLocaleNumber.value != localeNumber
    }

    /**
     * Save chosen localization in DataStorage.
     */
    fun setLocale() = runBlocking {
        settingsDataStorage.setLanguage(newLocaleNumber.value)
    }
}

