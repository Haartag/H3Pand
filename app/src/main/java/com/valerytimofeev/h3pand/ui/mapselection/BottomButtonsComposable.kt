package com.valerytimofeev.h3pand.ui.mapselection

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.sharp.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.valerytimofeev.h3pand.R

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
        HelpButton(navController)
        Spacer(modifier = Modifier.width(8.dp))
        AboutButton(navController)
        Spacer(modifier = Modifier.width(8.dp))
        ContactButton(navController)
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
fun HelpButton(
    navController: NavController
) {
    IconButton(
        modifier = Modifier.size(52.dp),
        onClick = { navController.navigate("help_screen") }
    ) {
        Icon(
            painterResource(id = R.drawable.ic_baseline_help_outline_24),
            contentDescription = "Help",
            modifier = Modifier.size(52.dp)
        )
    }
}

@Composable
fun ContactButton(
    navController: NavController
) {
    IconButton(
        modifier = Modifier.size(52.dp),
        onClick = { navController.navigate("contact_screen") }
    ) {
        Icon(
            Icons.Default.MailOutline,
            contentDescription = "Contacts",
            modifier = Modifier.size(52.dp)
        )
    }
}

@Composable
fun AboutButton(
    navController: NavController
) {
    IconButton(
        modifier = Modifier.size(52.dp),
        onClick = { navController.navigate("about_screen") }
    ) {
        Icon(
            Icons.Outlined.Info,
            contentDescription = "About application",
            modifier = Modifier.size(52.dp)
        )
    }
}