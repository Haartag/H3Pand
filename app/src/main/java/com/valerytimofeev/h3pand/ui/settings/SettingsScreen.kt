package com.valerytimofeev.h3pand.ui.settings


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.valerytimofeev.h3pand.ui.theme.SandboxButtonColor
import com.valerytimofeev.h3pand.ui.theme.TypographyCondenced

@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            SettingsListItem { LanguageSettings() }
        }
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.fillMaxHeight(0.85f))
            if (settingsViewModel.snackBarVisible.value) {
                LanguageConfirmBox(navController)
            }
        }
    }
}

@Composable
fun LanguageSettings(
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = settingsViewModel.languageText,
            style = TypographyCondenced.body1
        )

        Row(
            modifier = Modifier.clickable {
                settingsViewModel.languageDropDownState.value = true
            }
        ) {
            Text(
                text = settingsViewModel.currentLanguage.value,
                style = TypographyCondenced.body1
            )
            Box {
                Icon(
                    Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown menu icon"
                )
                DropdownMenu(
                    expanded = settingsViewModel.languageDropDownState.value,
                    onDismissRequest = { settingsViewModel.languageDropDownState.value = false }
                ) {

                    settingsViewModel.languages.forEachIndexed { index, item ->
                        DropdownMenuItem(
                            onClick = {
                                settingsViewModel.currentLanguage.value = item
                                settingsViewModel.setLanguage(index)
                                settingsViewModel.languageDropDownState.value = false
                            }
                        ) {
                            Text(
                                text = item,
                                style = TypographyCondenced.body1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsListItem(
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(96.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(1.dp)
                .background(Color.Gray)
        )
        content()
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(1.dp)
                .background(Color.Gray)
        )
    }
}

@Composable
fun LanguageConfirmBox(
    navController: NavController,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    Snackbar(
        //modifier = Modifier.padding(12.dp),
        action = {
            Text(
                text = settingsViewModel.sandboxButtonText,
                style = TypographyCondenced.body2.copy(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Center,
                color = SandboxButtonColor,
                modifier = Modifier
                    .fillMaxWidth(0.25f)
                    .clickable {
                        settingsViewModel.setLocal()
                        navController.navigate("splash_screen")
                    }
            )
        }
    ) {
            Text(
            text = settingsViewModel.sandboxText,
            style = TypographyCondenced.body2,
            modifier = Modifier.fillMaxWidth(0.75f)
        )
    }
}
