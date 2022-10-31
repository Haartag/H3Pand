package com.valerytimofeev.h3pand.ui.common_composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun CommonListItem(
    height: Dp = 96.dp,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(1.dp)
                .background(Color.Gray)
        )
        content()
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(1.dp)
                .background(Color.Gray)
        )
    }
}