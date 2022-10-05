package com.valerytimofeev.h3pand.ui.pandcalculation.pandcalculationcomposables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.valerytimofeev.h3pand.ui.pandcalculation.PandCalculationViewModel
import com.valerytimofeev.h3pand.utils.GuardRanges
import com.valerytimofeev.h3pand.utils.Values

@ExperimentalMaterialApi
@Composable
fun DialogScreen(
    viewModel: PandCalculationViewModel = hiltViewModel(),
    header: @Composable () -> Unit,
    text: @Composable () -> Unit,

    ) {
    NoPaddingAlertDialog(
        title = {
            header()
            Spacer(modifier = Modifier.height(20.dp))
        },
        text = {
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                text()
            }
        },
        onDismissRequest = {
            viewModel.closeDialogAndWipeData()
        },
        modifier = Modifier.height(384.dp)
    )
}

@Composable
fun ChooseGuardStage1(
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(viewModel.castleListSize) { index ->
            DialogTextRow(
                rowIndex = index,
                textList = viewModel.castleNamesList,
                onClick = { clickedItem ->
                    viewModel.chooseGuardDialogGoToStage2(clickedItem)
                }
            )
        }
    }
}

@Composable
fun ChooseGuardStage2(
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(viewModel.getRowCount(viewModel.guardList.size)) { index ->
            DialogTextRow(
                rowIndex = index,
                textList = viewModel.guardList.map { it.name },
                onClick = { clickedItem ->
                    viewModel.chooseGuardDialogGoToStage3(clickedItem)
                })
        }
    }
}

@Composable
fun ChooseGuardStage3(
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GuardRanges.range.forEach {
            Text(
                text = it.value.toString(),
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        viewModel.closeChooseGuardDialog(it.key)
                    }
            )
        }
    }
}

@Composable
fun AddValueDialogStage1(
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        viewModel.additionalValueTypesList.forEach {
            Text(
                text = it,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        when (it) {
                            "Dwelling" -> {
                                viewModel.getDwellingList()
                                viewModel.setDialogState("DwellingDialog")
                            }
                            "Custom value" -> {
                                viewModel.setDialogState("CustomValueDialog")
                            }
                            else -> {
                                viewModel.additionalValueType.value = it
                                viewModel.getAdditionalValueSubtypesList(it)
                                viewModel.setDialogState("AddValueStageTwo")
                            }
                        }
                    }
            )
        }
    }
}

@Composable
fun AddValueDialogStage2(
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        viewModel.additionalValueSubtypesList.forEach {
            Text(
                text = it,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        viewModel.additionalValueSubtype.value = it
                        viewModel.getAdditionalValueList()
                        viewModel.setDialogState("AddValueStageThree")
                    }
            )
        }
    }
}

@Composable
fun AddValueDialogStage3(
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(viewModel.additionalValueList.size) {
            Text(
                text = viewModel.additionalValueList[it].name,
                modifier = Modifier.clickable {
                    viewModel.closeAddValueDialog(viewModel.additionalValueList[it].value)
                    viewModel.closeDialogAndWipeData()
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun DwellingDialog(
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(viewModel.getRowCount(viewModel.dwellingList.size)) { index ->
            DialogTextRow(
                rowIndex = index,
                textList = viewModel.dwellingList.map { it.dwellingName },
                onClick = { clickedItem ->
                    viewModel.closeDwellingDialog(
                        viewModel.getDwellingValue(clickedItem)
                    )
                    viewModel.closeDialogAndWipeData()
                })
        }
    }
}

@Composable
fun CustomValueDialog(
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(viewModel.getRowCount(Values.valueList.size)) { index ->
            DialogTextRow(
                rowIndex = index,
                textList = Values.valueList.map { it.toString() },
                onClick = { clickedItem ->
                    viewModel.closeCustomValueDialog(
                        Values.valueList[clickedItem]
                    )
                    viewModel.closeDialogAndWipeData()
                })
        }
    }
}

@Composable
fun CastleZoneDialog(
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        repeat(viewModel.castleListSize) { index ->
            DialogTextRow(
                rowIndex = index,
                textList = viewModel.castleNamesList,
                onClick = { clickedItem ->
                    if (clickedItem == 10) {
                        viewModel.chosenCastleZone.value = 0
                    } else {
                        viewModel.chosenCastleZone.value = clickedItem + 1
                    }
                    viewModel.setDialogState("Close")
                    viewModel.getBoxesList()
                }
            )
        }
    }
}


@Composable
fun DialogTextRow(
    rowIndex: Int,
    textList: List<String>,
    onClick: (Int) -> Unit
) {
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onClick(rowIndex * 2) },
                text = textList[rowIndex * 2],
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(2.dp))
            if (textList.size >= rowIndex * 2 + 2) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onClick(rowIndex * 2 + 1) },
                    text = textList[rowIndex * 2 + 1],
                    textAlign = TextAlign.Center
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

//AlertDialog without unnecessary paddings
@Composable
fun NoPaddingAlertDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: @Composable (() -> Unit)? = null,
    text: @Composable (() -> Unit)? = null,
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    properties: DialogProperties = DialogProperties(),
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        Surface(
            modifier = modifier,
            shape = shape,
            color = backgroundColor,
            contentColor = contentColor
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                title?.let {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                        val textStyle = MaterialTheme.typography.subtitle1
                        ProvideTextStyle(textStyle, it)
                    }
                }
                text?.let {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                        val textStyle = MaterialTheme.typography.subtitle1
                        ProvideTextStyle(textStyle, it)
                    }
                }
            }
        }
    }
}