package com.valerytimofeev.h3pand.ui.help

import androidx.lifecycle.ViewModel
import com.valerytimofeev.h3pand.data.local.additional_data.TextStorage
import com.valerytimofeev.h3pand.data.local.additional_data.HelpScreenTexts
import com.valerytimofeev.h3pand.domain.use_case.GetLocalizedTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor(
    val getLocalizedTextUseCase: GetLocalizedTextUseCase
): ViewModel() {

    val helpTitleText = getLocalizedTextUseCase(TextStorage.HelpTitle.text)

    val helpBoxes = HelpScreenTexts.values()

}