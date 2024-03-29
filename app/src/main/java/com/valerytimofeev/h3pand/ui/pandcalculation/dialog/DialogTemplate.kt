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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.valerytimofeev.h3pand.ui.theme.Typography
import com.valerytimofeev.h3pand.ui.theme.TypographyCondenced


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
        body = {
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
        modifier = Modifier.height(432.dp)
    )
}

/**
 * 1-column row for dialog body.
 */
@Composable
fun DialogTextItem(
    text: String,
    horizontalPadding: Dp,
    onClick: () -> Unit,
    height: Dp = 40.dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = 4.dp)
            .height(height)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
        )
    }
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .clickable { onClick(rowIndex * 2) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = textList[rowIndex * 2],
                textAlign = TextAlign.Center,
            )
        }
        Spacer(modifier = Modifier.width(2.dp))
        if (textList.size >= rowIndex * 2 + 2) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .clickable { onClick(rowIndex * 2 + 1) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = textList[rowIndex * 2 + 1],
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }
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
    body: @Composable (() -> Unit)? = null,
    confirmButton: @Composable (() -> Unit)? = null,
    dismissButton: @Composable (() -> Unit)? = null,
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
                body?.let {
                    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                        val textStyle = TypographyCondenced.body1
                        ProvideTextStyle(textStyle, it)
                    }
                }
                //Buttons for dialog
                if (confirmButton != null || dismissButton != null) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (dismissButton != null) {
                                dismissButton()
                            }
                            Spacer(modifier = Modifier.width(1.dp))
                            if (confirmButton != null) {
                                confirmButton()
                            }
                        }
                    }
                }
            }
        }
    }
}