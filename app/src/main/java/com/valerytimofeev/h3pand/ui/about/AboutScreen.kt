package com.valerytimofeev.h3pand.ui.about

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.valerytimofeev.h3pand.BuildConfig
import com.valerytimofeev.h3pand.ui.topbar.MainTopBar

@Composable
fun AboutScreen(
    navController: NavController,
    aboutViewModel: AboutViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
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
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            //What`s new
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.body1,
                    text = aboutViewModel.aboutWhatsNewText
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.body2,
                    text = aboutViewModel.pointOne
                )
                Spacer(modifier = Modifier.weight(1f))
                ClickableText(
                    text = AnnotatedString("Open source libraries used in this app."),
                    style = MaterialTheme.typography.caption.merge(
                        TextStyle(
                            textAlign = TextAlign.Center,
                            textDecoration = TextDecoration.Underline,
                            color = Color.DarkGray
                        )
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate("about_licenses_screen")
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    text = "Application version: ${BuildConfig.VERSION_NAME}",
                    style = MaterialTheme.typography.caption,
                    textAlign = TextAlign.End,
                    color = Color.Gray
                )
            }
        }
    }
}