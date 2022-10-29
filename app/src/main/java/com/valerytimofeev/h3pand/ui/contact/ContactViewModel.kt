package com.valerytimofeev.h3pand.ui.contact

import androidx.lifecycle.ViewModel
import com.valerytimofeev.h3pand.data.additional_data.TextStorage
import com.valerytimofeev.h3pand.domain.use_case.GetLocalizedTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    getLocalizedTextUseCase: GetLocalizedTextUseCase
): ViewModel() {

    val contactTitleText = getLocalizedTextUseCase(TextStorage.ContactTitle.text)
}
