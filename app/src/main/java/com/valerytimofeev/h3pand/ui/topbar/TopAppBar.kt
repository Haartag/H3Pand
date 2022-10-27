package com.valerytimofeev.h3pand.ui.topbar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.valerytimofeev.h3pand.ui.theme.Typography

@Composable
fun MainTopBar(
    title: String = "",
    titleStyle: TextStyle = Typography.h5,
    buttonIcon: @Composable () -> Unit = {},
    onButtonClicked: (() -> Unit)? = null,
    backgroundColor: Color = Color.LightGray,
    additionalInfo: @Composable () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = titleStyle,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {
            if (onButtonClicked != null) {
                IconButton(onClick = { onButtonClicked() }) {
                    buttonIcon()
                }
            } else {
                buttonIcon()
            }
        },
        actions = {
            additionalInfo()
        },
        backgroundColor = backgroundColor
    )
}