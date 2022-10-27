package com.valerytimofeev.h3pand.ui.splashscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.valerytimofeev.h3pand.R
import com.valerytimofeev.h3pand.ui.theme.*

@Composable
fun SplashScreen(
    navController: NavController,
    splashViewModel: SplashScreenViewModel = hiltViewModel()
) {

    if (splashViewModel.dataLoaded.value) {
        splashViewModel.dataLoaded.value = false
        navController.navigate("map_selection_screen")
    }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(
                listOf(Color.White, SplashScreenGradient)
            )),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = "Heroes Helper",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth(),
                style = TitleTypo.h1,
            )
            Spacer(modifier = Modifier.height(screenWidth / 12))
            Box(
                modifier = Modifier
                    .padding(horizontal = screenWidth / 3),
                contentAlignment = Alignment.Center
            ) {
                val painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = R.drawable.ic_pand_box)
                        .build()
                )
                Image(
                    painter = painter,
                    contentDescription = "placeholder",
                    modifier = Modifier,
                )
            }
            Text(
                text = "by Faustos.",
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidth / 3)
                    .offset(
                        x = screenWidth / 6,
                    )
            )
        }
    }
}