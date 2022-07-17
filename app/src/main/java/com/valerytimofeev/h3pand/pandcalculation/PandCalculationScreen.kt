package com.valerytimofeev.h3pand.pandcalculation

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun PandCalculationScreen(
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    
    Text(text = viewModel.testString.value)

}