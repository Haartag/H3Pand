package com.valerytimofeev.h3pand.ui.mapselection

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.sharp.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BottomButtonRow(
    navController: NavController
) {
    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        SettingsButton(navController)
        Spacer(modifier = Modifier.width(8.dp))
        AboutButton()
    }
}

@Composable
fun SettingsButton(
    navController: NavController
) {
    IconButton(
        modifier = Modifier.size(52.dp),
        onClick = { navController.navigate("settings_screen") }
    ) {
        Icon(
            Icons.Sharp.Settings,
            contentDescription = "Settings",
            modifier = Modifier.size(52.dp)
        )
    }
}

@Composable
fun AboutButton(
) {
    IconButton(
        modifier = Modifier.size(52.dp),
        onClick = {  }
    ) {
        Icon(
            Icons.Outlined.Info,
            contentDescription = "About application",
            modifier = Modifier.size(52.dp)
        )
    }
}