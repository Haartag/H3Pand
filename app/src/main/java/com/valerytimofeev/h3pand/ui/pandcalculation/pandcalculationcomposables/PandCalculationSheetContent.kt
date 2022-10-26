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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.valerytimofeev.h3pand.domain.model.CastleSettings
import com.valerytimofeev.h3pand.domain.model.DialogState
import com.valerytimofeev.h3pand.ui.pandcalculation.PandCalculationViewModel
import com.valerytimofeev.h3pand.ui.pandcalculation.dialog.DialogViewModel
import com.valerytimofeev.h3pand.ui.theme.*
import kotlin.math.roundToInt

/**
 * Bottom sheet of pand calculation screen.
 */

@Composable
fun SheetContent(
    screenWidth: Dp,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SheetChooseUnit(screenWidth)
            SheetAdditionalValue(screenWidth)
        }
        Spacer(modifier = Modifier.height(16.dp))
        SheetChooseWeek()
        SheetChooseZoneCastle(screenWidth)
        Spacer(modifier = Modifier.height(16.dp))
        BottomSliderBlock()
    }
}

@Composable
fun SheetChooseUnit(
    screenWidth: Dp,
    viewModel: PandCalculationViewModel = hiltViewModel(),
    dialogViewModel: DialogViewModel = hiltViewModel(),
) {
    Box(
        modifier = Modifier
            .size((screenWidth - 48.dp) / 3)
            .clickable {
                viewModel.closeError()
                dialogViewModel.getChosenCastleZone(viewModel.chosenCastleZone.value)
                dialogViewModel.setDialogState(DialogState.Companion.DialogUiPresets.GUARD_CASTLE.dialogUiState)
            }
    ) {
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .data(data = viewModel.currentGuardImg.value)
                .scale(scale = Scale.FIT)
                .build()
        )
        Image(
            painter = painter,
            contentDescription = "placeholder",
        )
        Column() {

            Spacer(modifier = Modifier.weight(1f))
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
                Text(
                    text = viewModel.unitButtonText,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = TypographyCondenced.body1
                )
            }
        }

    }
}

@Composable
fun SheetAdditionalValue(
    screenWidth: Dp,
    viewModel: PandCalculationViewModel = hiltViewModel(),
    dialogViewModel: DialogViewModel = hiltViewModel()
) {
    val itemSize = (screenWidth - 48.dp) / 3
    Box(
        modifier = Modifier
            .width((itemSize.value * 1.75).dp)
            .height(itemSize)
    ) {
        Column(
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = viewModel.totalValueText,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .width(itemSize - 24.dp),
                style = TypographyCondenced.body1
            )
            Text(
                text = "${viewModel.getValueSum()}",
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .width(itemSize - 12.dp),
                style = TypographyCondenced.body1
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            repeat(2) { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(4) { column ->
                        //Skip first 2 buttons for text place
                        if (row == 0 && column < 2) {
                            Spacer(
                                modifier = Modifier
                                    .width((itemSize.value / 2.5).dp)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .width((itemSize.value / 2.5).dp)
                                    .background(
                                        color = if (viewModel.additionalValueMap.getOrDefault(
                                                row * 4 + column,
                                                defaultValue = 0
                                            ) == 0
                                        ) {
                                            AddValueBackground1Color
                                        } else {
                                            AddValueBackground2Color
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
    Column(
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        Text(
            text = viewModel.weekAndMonthText,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1
        )
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

//Button to select main castle of the current zone
@Composable
fun SheetChooseZoneCastle(
    screenWidth: Dp,
    modifier: Modifier = Modifier,
    viewModel: PandCalculationViewModel = hiltViewModel(),
    dialogViewModel: DialogViewModel = hiltViewModel(),
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = viewModel.mainTownText,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.subtitle1
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = modifier
                .size((screenWidth - 48.dp) / 3)
                .clickable {
                    viewModel.closeError()
                    dialogViewModel.setDialogState(DialogState.Companion.DialogUiPresets.ZONE.dialogUiState)
                }
        ) {
            val painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(
                        data = CastleSettings.values()
                            .find { it.id == viewModel.chosenCastleZone.value }?.img
                    )
                    .scale(scale = Scale.FIT)
                    .build()
            )
            Image(
                painter = painter,
                contentDescription = "placeholder",
            )
        }
    }
}


@Composable
fun BottomSliderBlock() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(horizontal = 20.dp)) {
            SheetChooseCastleNumber(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(16.dp))
            SheetChooseZone(modifier = Modifier.weight(1f))
        }
    }

}

//Slider to select the number of zones with a castle identical to the current zone
@Composable
fun SheetChooseCastleNumber(
    modifier: Modifier = Modifier,
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Column(modifier = modifier) {
        Text(
            text = viewModel.townZoneNumberText,
            style = TypographyCondenced.body1
        )
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
            text = viewModel.typeOfZoneText,
            style = TypographyCondenced.body1
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

