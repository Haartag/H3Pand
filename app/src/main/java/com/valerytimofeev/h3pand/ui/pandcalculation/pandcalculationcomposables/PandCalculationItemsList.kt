package com.valerytimofeev.h3pand.ui.pandcalculation.pandcalculationcomposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.valerytimofeev.h3pand.ui.pandcalculation.PandCalculationViewModel
import com.valerytimofeev.h3pand.ui.theme.Typography
import com.valerytimofeev.h3pand.ui.theme.TypographyCondenced

@Composable
fun ItemsListWithGroups(
    screenHeight: Dp,
    bottomSheetHeight: Dp,
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .height(screenHeight - bottomSheetHeight - 56.dp)
            .verticalScroll(rememberScrollState())
    ) {
        repeat(viewModel.boxGroups.size) {
            ItemGroup(
                index = it,
                img = viewModel.boxGroups[it].img,
                tileType = viewModel.tileTypeText(viewModel.boxGroups[it].type),
                tilePercent = viewModel.tilePercentText(it),
                backgroundColor = viewModel.getItemColor(viewModel.boxGroups[it].img),
                onClick = {
                    viewModel.expandedTiles[it] = !viewModel.expandedTiles.getOrDefault(it, false)
                },
                isExpanded = viewModel.expandedTiles[it] ?: false
            )
        }
    }
}

@Composable
fun ItemsListWithoutGroups(
    screenHeight: Dp,
    bottomSheetHeight: Dp,
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Box(modifier = Modifier.height(screenHeight - bottomSheetHeight - 56.dp)) {
        LazyColumn(contentPadding = PaddingValues(12.dp)) {
            items(count = viewModel.boxesWithPercents.size) {
                ItemEntry(
                    img = viewModel.boxesWithPercents[it].img,
                    nameText = viewModel.itemNameText(viewModel.boxesWithPercents[it].name),
                    percentText = viewModel.itemPercentText(viewModel.boxesWithPercents[it].dropChance),
                    guardText = viewModel.itemGuardRangeText(viewModel.boxesWithPercents[it].range),
                    mostLikelyText = viewModel.itemMostLikelyText(viewModel.boxesWithPercents[it].mostLikelyGuard)
                )
            }
        }
    }
}

@Composable
fun ItemGroup(
    index: Int,
    img: String,
    tileType: String,
    tilePercent: String,
    backgroundColor: Color,
    onClick: () -> Unit,
    isExpanded: Boolean,
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .background(color = backgroundColor)
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .height(80.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            val painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(viewModel.getItemImage(img))
                    .build()
            )
            Image(
                painter = painter,
                contentDescription = viewModel.boxGroups[index].type,
                contentScale = ContentScale.FillHeight,
                alpha = 0.6f
            )
        }
        Column(
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = tileType,
                    style = Typography.subtitle1
                )
                Text(
                    text = tilePercent,
                    style = Typography.h6
                )
            }
            IconButton(onClick = { onClick() }) {
                Icon(
                    if (isExpanded) {
                        Icons.Default.KeyboardArrowDown
                    } else {
                        Icons.Default.KeyboardArrowRight
                    },
                    contentDescription = ""
                )
            }
            if (isExpanded) {
                Spacer(modifier = Modifier.height(24.dp))
                AdditionalPart(
                    index = index,
                    color = backgroundColor
                )
                IconButton(onClick = { onClick() }) {
                    Icon(
                        Icons.Default.KeyboardArrowUp,
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

@Composable
fun AdditionalPart(
    index: Int,
    color: Color,
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .background(color)
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column() {
                viewModel.boxGroups[index].content.forEach {
                    ItemEntry(
                        img = it.img,
                        nameText = viewModel.itemNameText(it.name),
                        percentText = viewModel.itemPercentText(it.dropChance),
                        guardText = viewModel.itemGuardRangeText(it.range),
                        mostLikelyText = viewModel.itemMostLikelyText(it.mostLikelyGuard)
                    )
                }
            }
        }
    }
}

@Composable
fun ItemEntry(
    img: String,
    nameText: String,
    percentText: String,
    guardText: String,
    mostLikelyText: String,
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .shadow(elevation = 2.dp, RoundedCornerShape(4.dp))
            .clip(RoundedCornerShape(4.dp))
            .background(color = viewModel.getItemColor(img, true))
            .fillMaxWidth()
            .height(88.dp)
            .padding(8.dp),
    ) {
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .data(viewModel.getItemImage(img))
                .build()
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painter,
                contentDescription = "",
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
                Text(
                    text = nameText,
                    style = TypographyCondenced.body1
                )
                Text(
                    text = percentText,
                    style = TypographyCondenced.body1
                )
            }
            Spacer(modifier = Modifier.weight(0.8f))
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = guardText,
                    style = TypographyCondenced.body1,
                    textAlign = TextAlign.End
                )
                Text(
                    text = mostLikelyText,
                    style = TypographyCondenced.body1,
                    textAlign = TextAlign.End
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}