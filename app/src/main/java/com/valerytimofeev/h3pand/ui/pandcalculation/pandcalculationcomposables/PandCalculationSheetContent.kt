package com.valerytimofeev.h3pand.ui.pandcalculation.pandcalculationcomposables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.valerytimofeev.h3pand.data.additional_data.GuardRanges
import com.valerytimofeev.h3pand.domain.model.CastleSettings
import com.valerytimofeev.h3pand.domain.model.DialogState
import com.valerytimofeev.h3pand.ui.pandcalculation.PandCalculationViewModel
import com.valerytimofeev.h3pand.ui.pandcalculation.dialog.DialogViewModel
import com.valerytimofeev.h3pand.ui.theme.SliderColor
import com.valerytimofeev.h3pand.ui.theme.SliderColorSecondary
import kotlin.math.roundToInt

/**
 * Bottom sheet of pand calculation screen.
 */

@Composable
fun SheetContent(
    screenWidth: Dp
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row {
            SheetChooseUnit(modifier = Modifier.weight(2f))
            SheetAdditionalValue(
                modifier = Modifier.weight(3f),
                screenWidth = screenWidth / 2
            )
        }
        SheetChooseWeek()
        Row {
            SheetChooseZoneCastle(
                modifier = Modifier.weight(1f),
                halfWidthOfScreen = screenWidth / 2
            )
            Column(modifier = Modifier.weight(1f)) {
                SheetChooseCastleNumber()
                SheetChooseZone()
            }
        }
    }
}

//Button with img to open dialog for selecting guard
@Composable
fun SheetChooseUnit(
    modifier: Modifier = Modifier,
    viewModel: PandCalculationViewModel = hiltViewModel(),
    dialogViewModel: DialogViewModel = hiltViewModel(),
) {
    Box(
        modifier = modifier
            .height(100.dp)
            .fillMaxWidth()
    ) {
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .data(data = viewModel.currentGuardImg.value)
                .scale(scale = Scale.FILL)
                .build()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .clickable {
                    viewModel.closeError()
                    dialogViewModel.getChosenCastleZone(viewModel.chosenCastleZone.value)
                    dialogViewModel.setDialogState(DialogState.Companion.DialogUiPresets.GUARD_CASTLE.dialogUiState)
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painter,
                contentDescription = "placeholder",
                contentScale = ContentScale.Fit,
                modifier = Modifier,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        CastleSettings
                            .values()
                            .find { it.id == viewModel.chosenCastleZone.value }?.sheetColor?.copy(
                                alpha = 0.6f
                            )
                            ?: MaterialTheme.colors.secondary,
                    )
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = viewModel.getLocalizedTextUseCase(viewModel.chosenGuard.value),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = GuardRanges.range.getOrDefault(viewModel.chosenGuardRange.value, "")
                            .toString(),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

//Field with buttons to open dialog for selecting additional value
@Composable
fun SheetAdditionalValue(
    modifier: Modifier = Modifier,
    screenWidth: Dp,
    viewModel: PandCalculationViewModel = hiltViewModel(),
    dialogViewModel: DialogViewModel = hiltViewModel()
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = viewModel.totalValueText,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .width(screenWidth / 4 + 40.dp)
            )
            Text(
                text = "${ viewModel.getValueSum() }",
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .width(screenWidth / 4 + 40.dp)
            )
        }
        Column() {
            repeat(2) { row ->
                Row() {
                    repeat(4) { column ->
                        //Skip first 2 buttons for text place
                        if (row == 0 && column < 2) {
                            Spacer(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .width(screenWidth / 8 + 16.dp)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(screenWidth / 8 + 16.dp)
                                    .background(
                                        color = if (viewModel.additionalValueMap.getOrDefault(
                                                row * 4 + column,
                                                defaultValue = 0
                                            ) == 0
                                        ) {
                                            Color.Gray
                                        } else {
                                            Color.Red
                                        }
                                    )
                                    .clickable {
                                        viewModel.closeError()
                                        if (viewModel.addOrRemoveAddValue(
                                                row = row,
                                                column = column
                                            )
                                        ) {
                                            dialogViewModel.getChosenCastleZone(viewModel.chosenCastleZone.value)
                                            dialogViewModel.getAddValueSlot(row * 4 + column)
                                            dialogViewModel.setDialogState(
                                                DialogState.Companion.DialogUiPresets.ADDVALUE_TYPE.dialogUiState
                                            )
                                        }
                                    }
                            ) {
                                val painter = rememberAsyncImagePainter(
                                    ImageRequest.Builder(LocalContext.current)
                                        /*.data(
                                            data = CastleSettings.values()
                                                .find { it.id == viewModel.chosenCastleZone.value }?.img
                                        )*/
                                        //.data(R.drawable.ic_question)
                                        .data(viewModel.getAddValueImage(row * 4 + column))
                                        .scale(scale = Scale.FILL)
                                        .build()
                                )
                                Image(
                                    painter = painter,
                                    contentDescription = "placeholder",
                                    contentScale = ContentScale.Fit,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

//Slider to choose current week
@Composable
fun SheetChooseWeek(
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Column() {
        Text(text = viewModel.weekAndMonthText)
        Slider(
            value = viewModel.weekSliderPosition.value,
            valueRange = 0f..8f,
            steps = 7,
            onValueChange = {
                viewModel.weekSliderPosition.value = it
            },
            onValueChangeFinished = {
                viewModel.closeError()
                viewModel.getBoxesList()
            },
            colors = SliderDefaults.colors(
                thumbColor = SliderColor,
                activeTrackColor = SliderColor,
                inactiveTrackColor = SliderColorSecondary,
                activeTickColor = SliderColorSecondary,
                inactiveTickColor = SliderColor
            )
        )
    }
}

//Slider to select the number of zones with a castle identical to the current zone
@Composable
fun SheetChooseCastleNumber(
    modifier: Modifier = Modifier,
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Column(modifier = modifier) {
        Text(text = viewModel.townZoneNumberText)
        Slider(
            value = viewModel.castlesSliderPosition.value,
            valueRange = 1f..viewModel.maxCastleNumber,
            steps = viewModel.castleSliderSteps,
            onValueChange = {
                viewModel.castlesSliderPosition.value = it
            },
            onValueChangeFinished = {
                viewModel.closeError()
                viewModel.getBoxesList()
            },
            colors = SliderDefaults.colors(
                thumbColor = SliderColor,
                activeTrackColor = SliderColor,
                inactiveTrackColor = SliderColorSecondary,
                activeTickColor = SliderColorSecondary,
                inactiveTickColor = SliderColor
            )
        )
    }
}

//Slider to select current zone type (ex. "resp", "mid")
@Composable
fun SheetChooseZone(
    modifier: Modifier = Modifier,
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Column(modifier = modifier) {
        Text(
            text = viewModel.typeOfZoneText
        )
        Slider(
            value = viewModel.zoneSliderPosition.value,
            valueRange = 0f..1f,
            //steps = 1,
            onValueChange = {
                viewModel.zoneSliderPosition.value = it
            },
            onValueChangeFinished = {
                viewModel.closeError()
                viewModel.zoneSliderPosition.value =
                    viewModel.zoneSliderPosition.value.roundToInt().toFloat()
                viewModel.getBoxesList()
            },
            colors = SliderDefaults.colors(
                thumbColor = SliderColor,
                activeTrackColor = SliderColor,
                inactiveTrackColor = SliderColorSecondary,
                activeTickColor = SliderColorSecondary,
                inactiveTickColor = SliderColor
            )
        )
    }
}

//Button to select main castle of the current zone
@Composable
fun SheetChooseZoneCastle(
    halfWidthOfScreen: Dp,
    modifier: Modifier = Modifier,
    viewModel: PandCalculationViewModel = hiltViewModel(),
    dialogViewModel: DialogViewModel = hiltViewModel(),
) {
    Column() {
        Text(text = viewModel.mainTownText)
        Box(
            modifier = modifier
                .size(halfWidthOfScreen)
                .padding(12.dp)
                .padding(end = 12.dp)
        ) {
            val painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(
                        data = CastleSettings.values()
                            .find { it.id == viewModel.chosenCastleZone.value }?.img
                    )
                    .scale(scale = Scale.FILL)
                    .build()
            )
            Image(
                painter = painter,
                contentDescription = "placeholder",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .clickable {
                        viewModel.closeError()
                        dialogViewModel.setDialogState(DialogState.Companion.DialogUiPresets.ZONE.dialogUiState)
                    }
            )
        }
    }
}