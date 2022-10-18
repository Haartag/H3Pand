package com.valerytimofeev.h3pand.ui.splashscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.valerytimofeev.h3pand.R
import com.valerytimofeev.h3pand.ui.theme.Secondary

@Composable
fun SplashScreen(
    navController: NavController,
    splashViewModel: SplashScreenViewModel = hiltViewModel()
) {

    if (splashViewModel.dataLoaded.value) {
        splashViewModel.dataLoaded.value = false
        navController.navigate("map_selection_screen")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Secondary),
        contentAlignment = Alignment.Center
    ) {

        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .data(data = R.drawable.ic_icon)
                .build()
        )
        Image(
            painter = painter,
            contentDescription = "placeholder",
            modifier = Modifier,
        )
    }
}