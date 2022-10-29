package com.valerytimofeev.h3pand.ui.about

import androidx.lifecycle.ViewModel
import com.valerytimofeev.h3pand.data.additional_data.TextStorage
import com.valerytimofeev.h3pand.domain.use_case.GetLocalizedTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(
    getLocalizedTextUseCase: GetLocalizedTextUseCase
): ViewModel() {

    val aboutTitleText = getLocalizedTextUseCase(TextStorage.AboutTitle.text)
    val aboutWhatsNewText = getLocalizedTextUseCase(TextStorage.AboutWhatsNew.text)

    val pointOne = getLocalizedTextUseCase(TextStorage.AboutPointOne.text)

}