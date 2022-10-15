package com.valerytimofeev.h3pand.ui.pandcalculation.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.valerytimofeev.h3pand.ui.theme.Typography


/**
 * Generate [NoPaddingAlertDialog] with chosen header and body.
 */
@ExperimentalMaterialApi
@Composable
fun DialogScreen(
    dialogViewModel: DialogViewModel = hiltViewModel(),
    header: @Composable () -> Unit,
    body: @Composable () -> Unit,

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
                body()
            }
        },
        onDismissRequest = {
            dialogViewModel.closeDialogAndWipeData()
        },
        modifier = Modifier.height(384.dp)
    )
}

/**
 * 2-column row for dialog body.
 * Row number must be halved.
 */
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

/**
 * AlertDialog without unnecessary paddings
 */
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
                        val textStyle = Typography.body1
                        ProvideTextStyle(textStyle, it)
                    }
                }
                text?.let {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                        val textStyle = Typography.body1
                        ProvideTextStyle(textStyle, it)
                    }
                }
            }
        }
    }
}