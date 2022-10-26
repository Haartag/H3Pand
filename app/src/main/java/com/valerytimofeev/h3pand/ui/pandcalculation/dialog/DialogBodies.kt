package com.valerytimofeev.h3pand.ui.pandcalculation.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.valerytimofeev.h3pand.data.additional_data.GuardRanges
import com.valerytimofeev.h3pand.data.additional_data.Values
import com.valerytimofeev.h3pand.data.local.AdditionalValueItem
import com.valerytimofeev.h3pand.domain.model.DialogState
import com.valerytimofeev.h3pand.ui.pandcalculation.PandCalculationViewModel


@Composable
fun ChooseGuardStage1(
    dialogViewModel: DialogViewModel = hiltViewModel(),
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(dialogViewModel.getRowCount(viewModel.castleNamesList.size)) { index ->
            DialogTextRow(
                rowIndex = index,
                textList = viewModel.castleNamesList,
                onClick = { clickedItem ->
                    dialogViewModel.guardDialogCastleButton(clickedItem)
                }
            )
        }
    }
}

@Composable
fun ChooseGuardStage2(
    dialogViewModel: DialogViewModel = hiltViewModel(),
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(dialogViewModel.getRowCount(dialogViewModel.guardList.size)) { index ->
            DialogTextRow(
                rowIndex = index,
                textList = dialogViewModel.guardList.map {
                    dialogViewModel.getLocalizedTextUseCase(
                        it
                    )
                },
                onClick = { clickedItem ->
                    dialogViewModel.guardDialogUnitButton(clickedItem, dialogViewModel.guardList)
                })
        }
    }
}

@Composable
fun ChooseGuardStage3(
    dialogViewModel: DialogViewModel = hiltViewModel(),
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        GuardRanges.range.forEach {
            DialogTextItem(
                text = it.value.toString().replace("..", "–"),
                horizontalPadding = 100.dp,
                onClick = {
                    dialogViewModel.guardDialogNumberButton(it.key)
                    //Main viewmodel get data
                    viewModel.getGuardData(dialogViewModel.guardAndNumberOutput)
                }
            )
        }
    }
}

@Composable
fun SearchGuardDialog(
    dialogViewModel: DialogViewModel = hiltViewModel(),
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(dialogViewModel.searchGuardResult.size) { index ->
            DialogTextItem(
                text = dialogViewModel.searchGuardResultText[index],
                horizontalPadding = 48.dp,
                onClick = {
                    dialogViewModel.guardDialogUnitButton(
                        index,
                        dialogViewModel.searchGuardResult
                    )
                    dialogViewModel.setDialogState(
                        DialogState.Companion.DialogUiPresets.GUARD_NUMBER.dialogUiState
                    )
                }
            )
        }
    }
}

@Composable
fun AddValueDialogStage1(
    dialogViewModel: DialogViewModel = hiltViewModel(),
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        viewModel.additionalValueTypesList.forEach {
            DialogTextItem(
                text = dialogViewModel.getLocalizedTextUseCase(it),
                horizontalPadding = 48.dp,
                onClick = {
                    dialogViewModel.addValueTypeButton(
                        it.enText,
                        viewModel.chosenCastleZone.value
                    )
                }
            )
        }
    }
}

@Composable
fun AddValueDialogStage2(
    dialogViewModel: DialogViewModel = hiltViewModel(),
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        dialogViewModel.addValueSubtypeList.forEach {
            DialogTextItem(
                text = dialogViewModel.getLocalizedTextUseCase(it),
                horizontalPadding = 24.dp,
                onClick = {
                    dialogViewModel.addValueSubtypeButton(it)
                },
                height = 36.dp
            )
        }
    }
}

@Composable
fun AddValueDialogStage3(
    dialogViewModel: DialogViewModel = hiltViewModel(),
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(dialogViewModel.additionalValueList.size) {
            DialogTextItem(
                text = dialogViewModel.getLocalizedTextUseCase(
                    dialogViewModel.additionalValueList[it]
                ),
                horizontalPadding = 24.dp,
                onClick = {
                    viewModel.getAddValueData(
                        dialogViewModel.addValueItemButton(dialogViewModel.additionalValueList[it])
                    )
                }
            )
        }
    }
}

@Composable
fun DwellingDialog(
    dialogViewModel: DialogViewModel = hiltViewModel(),
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(dialogViewModel.getRowCount(dialogViewModel.dwellingList.size)) { index ->
            DialogTextRow(
                rowIndex = index,
                textList = dialogViewModel.dwellingList.map {
                    dialogViewModel.getLocalizedTextUseCase(it)
                },
                onClick = { clickedItem ->
                    viewModel.getDwellingData(
                        dialogViewModel.dwellingItemButton(dialogViewModel.dwellingList[clickedItem])
                    )
                    dialogViewModel.closeDialogAndWipeData()
                })
        }
    }
}

@Composable
fun CustomValueDialog(
    dialogViewModel: DialogViewModel = hiltViewModel(),
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(dialogViewModel.getRowCount(Values.valueList.size)) { index ->
            DialogTextRow(
                rowIndex = index,
                textList = Values.valueList.map { it.toString() },
                onClick = { clickedItem ->
                    viewModel.getAddValueData(
                        dialogViewModel.addValueItemButton(
                            AdditionalValueItem(
                                0,
                                Values.valueList.map { it.toString() }[clickedItem],
                                Values.valueList.map { it.toString() }[clickedItem],
                                Values.valueList[clickedItem],
                                0,
                                "Custom",
                                "Custom",
                                "Custom",
                                "Custom",
                                -1
                            )
                        )
                    )
                })
        }
    }
}

@Composable
fun SearchAddValueDialog(
    dialogViewModel: DialogViewModel = hiltViewModel(),
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(dialogViewModel.searchAddValueResult.size) {

            DialogTextItem(
                text = dialogViewModel.searchAddValueResultText[it],
                horizontalPadding = 48.dp,
                onClick = {
                    viewModel.getSearchItemData(
                        dialogViewModel.addValueSearchItemButton(it)
                    )
                    dialogViewModel.setDialogState(
                        DialogState.Companion.DialogUiPresets.CLOSED.dialogUiState
                    )
                }
            )
        }
    }
}

@Composable
fun CastleZoneDialog(
    dialogViewModel: DialogViewModel = hiltViewModel(),
    viewModel: PandCalculationViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        repeat(dialogViewModel.getRowCount(viewModel.castleNamesList.size)) { index ->
            DialogTextRow(
                rowIndex = index,
                textList = viewModel.castleNamesList,
                onClick = { clickedItem ->

                    viewModel.chosenCastleZone.value =
                        dialogViewModel.castleZoneDialogButton(clickedItem)
                    viewModel.getBoxesList()
                }
            )
        }
    }
}
