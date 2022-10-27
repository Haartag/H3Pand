package com.valerytimofeev.h3pand.ui.pandcalculation.pandcalculationcomposables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.valerytimofeev.h3pand.ui.pandcalculation.PandCalculationViewModel
import com.valerytimofeev.h3pand.ui.theme.ErrorBoxColor

@Composable
fun ErrorBlock(
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .background(ErrorBoxColor)
            .height(100.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = viewModel.errorText.value,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}