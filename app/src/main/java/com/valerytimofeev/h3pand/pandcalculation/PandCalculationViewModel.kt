package com.valerytimofeev.h3pand.pandcalculation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.valerytimofeev.h3pand.repositories.local.PandRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PandCalculationViewModel @Inject constructor(
    private val repository: PandRepository
) : ViewModel() {

    val testString = mutableStateOf(">")

    init {
        viewModelScope.launch {
            //testString.value = testString.value.plus(repository.getGuardById(1).toString())
            delay(1000L)
            //testString.value = testString.value.plus(repository.getGuardById(2).toString())
            delay(1000L)
            //testString.value = testString.value.plus(repository.getGuardById(3).toString())
            //testString.value = testString.value.plus(repository.getAdditionalValueByName(3).toString())
        }
    }
}