package com.valerytimofeev.h3pand.ui.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer
import com.valerytimofeev.h3pand.ui.topbar.MainTopBar

@Composable
fun AboutLicensesScreen(
    navController: NavController,
    aboutViewModel: AboutViewModel = hiltViewModel()
) {
    Column {
        MainTopBar(
            title = aboutViewModel.aboutTitleText,
            buttonIcon = {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back button",
                )
            },
            onButtonClicked = { navController.popBackStack() }
        )
        LibrariesContainer(
            Modifier.fillMaxSize()
        )
    }
}