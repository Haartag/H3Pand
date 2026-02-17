package com.valerytimofeev.h3pand.ui.ratedialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.valerytimofeev.h3pand.data.local.additional_data.TextStorage
import com.valerytimofeev.h3pand.domain.use_case.GetLocalizedTextUseCase
import com.valerytimofeev.h3pand.domain.use_case.RateRequestUseCase


/**
 * Rate dialog template, rebuild and implement it later
 */
@Composable
fun RateDialog(
    onDismiss: (Boolean) -> Unit,
) {

    val getLocalizedTextUseCase: GetLocalizedTextUseCase = GetLocalizedTextUseCase()
    val context = LocalContext.current

    AlertDialog(
        modifier = Modifier
            .fillMaxWidth(),
        onDismissRequest = { onDismiss(false) },
        title = {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = getLocalizedTextUseCase(TextStorage.RateRequestBody.text),
                )
            }
        },
        text = {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = ""
            )
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .clickable {
                            onDismiss(false)
                        },
                    text = getLocalizedTextUseCase(TextStorage.No.text)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    modifier = Modifier.clickable {
                        RateRequestUseCase().invoke(context)
                        onDismiss(false)
                    },
                    text = getLocalizedTextUseCase(TextStorage.Yes.text)
                )

            }
        }
    )
}