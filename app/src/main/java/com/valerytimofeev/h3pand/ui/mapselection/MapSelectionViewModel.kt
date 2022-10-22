package com.valerytimofeev.h3pand.ui.mapselection

import androidx.lifecycle.ViewModel
import com.valerytimofeev.h3pand.data.additional_data.MapSettings
import com.valerytimofeev.h3pand.data.additional_data.TextStorage
import com.valerytimofeev.h3pand.domain.use_case.GetLocalizedTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapSelectionViewModel @Inject constructor(
    getLocalizedTextUseCase: GetLocalizedTextUseCase
): ViewModel() {

    val maps = MapSettings.values()

    val mapTitle = getLocalizedTextUseCase(TextStorage.MapTitle.text)


}