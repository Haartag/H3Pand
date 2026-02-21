package com.valerytimofeev.h3pand.ui.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer
import com.valerytimofeev.h3pand.ui.topbar.MainTopBar

@Composable
fun AboutLicensesScreen(
    navController: NavController,
    aboutViewModel: AboutViewModel = hiltViewModel()
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .background(Color.White)
        )
    }
}