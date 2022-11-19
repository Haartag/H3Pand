package com.valerytimofeev.h3pand.ui.pandcalculation.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.valerytimofeev.h3pand.ui.pandcalculation.PandCalculationViewModel
import com.valerytimofeev.h3pand.ui.theme.Typography
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior

/**
 * Choose exact number of guard via [Spinner] in [NoPaddingAlertDialog].
 */
@ExperimentalSnapperApi
@Composable
fun SpecifyDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    viewModel: PandCalculationViewModel = hiltViewModel(),
) {

    NoPaddingAlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.DarkGray
                    ),
                    onClick = {
                        onConfirm()
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Confirm")
                    }
                }
            }
        },
        title = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                textAlign = TextAlign.Center,
                text = viewModel.specifyDialogTitleText,
                style = Typography.body1
            )
        },
        body = {
            Column() {
                Spinner(
                    items = viewModel.guardNumbersList,
                    onValueChange = { viewModel.exactlyGuardianNumber.value = it },
                    startNumber = viewModel.chosenGuardRange.value.first - 1,
                    additionalValues = 2
                )
            }
        }
    )
}

/**
 * Horizontal wheel picker. Need "snapper" dependency.
 * @param items list of items to display
 * @param onValueChange lambda callback, returns index of current selected item
 * @param startNumber which index is selected by default
 * @param additionalValues how many items will be shown to the right and left of the selected value
 */
@ExperimentalSnapperApi
@Composable
fun <T> Spinner(
    items: List<T>,
    onValueChange: (Int) -> Unit,
    startNumber: Int = 0,
    additionalValues: Int = 1
) {
    val firstItem = remember { mutableStateOf(startNumber) }
    val state = rememberLazyListState(firstItem.value)
    val snapper = rememberSnapperFlingBehavior(state)
    val list = items.map { it.toString() }.toMutableList()

    repeat(additionalValues) {
        list.add(0, "")
        list.add("")
    }

    val statePosition = remember { derivedStateOf { state.firstVisibleItemIndex } }
    var previousStatePosition = startNumber

    Column {
        LazyRow(
            state = state,
            flingBehavior = snapper,
        ) {
            if (statePosition.value != previousStatePosition) {
                onValueChange(statePosition.value)
                previousStatePosition = statePosition.value
            }
            items(list.size) {
                Box(
                    modifier = Modifier
                        .fillParentMaxWidth(1.0f / (1 + additionalValues * 2))
                        .height(40.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = list[it],
                        fontSize = if (it == (statePosition.value + additionalValues)) 20.sp else 14.sp,
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter,
        ) {
            Icon(
                Icons.Default.KeyboardArrowUp,
                contentDescription = "Selected item"
            )
        }
    }
}