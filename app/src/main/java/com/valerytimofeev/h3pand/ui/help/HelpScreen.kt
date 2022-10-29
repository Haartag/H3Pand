package com.valerytimofeev.h3pand.ui.help

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.valerytimofeev.h3pand.ui.topbar.MainTopBar

@Composable
fun HelpScreen(
    navController: NavController,
    helpViewModel: HelpViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        MainTopBar(
            title = helpViewModel.helpTitleText,
            buttonIcon = {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back button",
                )
            },
            onButtonClicked = { navController.popBackStack() }
        )

    }
}