package com.valerytimofeev.h3pand.ui.pandcalculation.dialog

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.valerytimofeev.h3pand.data.local.AdditionalValueItem
import com.valerytimofeev.h3pand.domain.model.DialogState
import com.valerytimofeev.h3pand.ui.pandcalculation.PandCalculationViewModel
import com.valerytimofeev.h3pand.data.additional_data.GuardRanges
import com.valerytimofeev.h3pand.data.additional_data.Values


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
            Text(
                text = it.value.toString(),
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        dialogViewModel.guardDialogNumberButton(it.key)
                        viewModel.getGuardData( //Main viewmodel get data
                            dialogViewModel.guardAndNumberOutput
                        )
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
            Text(
                text = dialogViewModel.searchGuardResultText[index],
                modifier = Modifier.clickable {
                    dialogViewModel.guardDialogUnitButton(index, dialogViewModel.searchGuardResult)
                    dialogViewModel.setDialogState(DialogState.Companion.DialogUiPresets.GUARD_NUMBER.dialogUiState)
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
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
            val string = dialogViewModel.getLocalizedTextUseCase(it)
            Text(
                text = string,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        Log.d("TestTag", "AddValueDialogStage1: ${it.enText}")
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
            val string = dialogViewModel.getLocalizedTextUseCase(it)
            Text(
                text = string,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        dialogViewModel.addValueSubtypeButton(it)
                    }
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
            val string = dialogViewModel.getLocalizedTextUseCase(dialogViewModel.additionalValueList[it])
            Text(
                text = string,
                modifier = Modifier.clickable {
                    viewModel.getAddValueData(
                        dialogViewModel.addValueItemButton(dialogViewModel.additionalValueList[it])
                    )
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
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
                                "custom",
                                "custom",
                                "custom",
                                "custom",
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
            Text(
                text = dialogViewModel.searchAddValueResultText[it],
                modifier = Modifier.clickable {
                    viewModel.getSearchItemData(
                        dialogViewModel.addValueSearchItemButton(it)
                    )
                    dialogViewModel.setDialogState(DialogState.Companion.DialogUiPresets.CLOSED.dialogUiState)
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
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
