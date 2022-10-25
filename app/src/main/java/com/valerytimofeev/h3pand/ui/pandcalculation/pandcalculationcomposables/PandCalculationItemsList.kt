package com.valerytimofeev.h3pand.ui.pandcalculation.pandcalculationcomposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.valerytimofeev.h3pand.ui.pandcalculation.PandCalculationViewModel


@Composable
fun ItemsList(
    screenHeight: Dp,
    bottomSheetHeight: Dp,
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Box(modifier = Modifier.height(screenHeight - bottomSheetHeight + 16.dp)) {
        LazyColumn(contentPadding = PaddingValues(12.dp)) {
            items(count = viewModel.boxesWithPercents.size) {
                ItemEntry(
                    itemIndex = it
                )
            }
        }
    }
}

@Composable
fun ItemEntry(
    itemIndex: Int,
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .shadow(elevation = 4.dp, RoundedCornerShape(4.dp))
            .clip(RoundedCornerShape(4.dp))
            .background(color = viewModel.getItemColor(itemIndex))
            .fillMaxWidth()
            .height(88.dp)
            .padding(8.dp),
    ) {
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .data(viewModel.getItemImage(itemIndex))
                .build()
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painter,
                contentDescription = viewModel.boxesWithPercents[itemIndex].name.enText,
                contentScale = ContentScale.FillHeight,
                alpha = 0.6f
            )
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(text = viewModel.itemNameText(itemIndex))
                Text(
                    text = "${viewModel.boxesWithPercents[itemIndex].dropChance} %",
                    style = MaterialTheme.typography.subtitle1
                )
            }
            Spacer(modifier = Modifier.weight(0.8f))
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(
                    text = viewModel.itemGuardRangeText(itemIndex),
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = viewModel.itemMostLikelyText(itemIndex),
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}