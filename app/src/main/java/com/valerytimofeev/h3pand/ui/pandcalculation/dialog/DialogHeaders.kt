package com.valerytimofeev.h3pand.ui.pandcalculation.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.valerytimofeev.h3pand.domain.model.SearchType

@Composable
fun SearchButtonHeader(
    type: SearchType,
    dialogViewModel: DialogViewModel = hiltViewModel()
) {
    Spacer(modifier = Modifier.height(16.dp))
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .clickable {
                    dialogViewModel.openSearch(
                        type = type
                    )
                }
        ) {
            Row {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier.offset(y = 4.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Search",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
fun SearchGuardHeader(
    dialogViewModel: DialogViewModel = hiltViewModel()
) {
    val focusRequester = FocusRequester()
    LaunchedEffect(Unit) {
        focusRequester.requestFocus() //Autofocus text field when it appears
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .focusRequester(focusRequester),
        value = dialogViewModel.searchGuardInput.value,
        onValueChange = {
            dialogViewModel.searchGuardInput.value = it
            dialogViewModel.searchGuard()
        }
    )
}

@Composable
fun SearchAddValueHeader(
    dialogViewModel: DialogViewModel = hiltViewModel()
) {
    val focusRequester = FocusRequester()
    LaunchedEffect(Unit) {
        focusRequester.requestFocus() //Autofocus text field when it appears
    }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .focusRequester(focusRequester),
        value = dialogViewModel.searchAddValueInput.value,
        onValueChange = {
            dialogViewModel.searchAddValueInput.value = it
            dialogViewModel.searchAddValue()
        }
    )
}










