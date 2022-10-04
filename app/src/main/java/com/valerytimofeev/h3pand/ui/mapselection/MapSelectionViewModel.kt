package com.valerytimofeev.h3pand.ui.mapselection

import androidx.lifecycle.ViewModel
import com.valerytimofeev.h3pand.utils.MapSettings

class MapSelectionViewModel: ViewModel() {

    val maps = MapSettings.values()

}