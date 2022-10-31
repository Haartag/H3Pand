package com.valerytimofeev.h3pand.ui.help

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.valerytimofeev.h3pand.ui.common_composables.CommonListItem
import com.valerytimofeev.h3pand.ui.theme.TypographyCondenced
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
        LazyColumn{
            items(count = helpViewModel.helpBoxes.size) {
                HelpListItem(
                    img = helpViewModel.helpBoxes[it].img,
                    imgDescription = helpViewModel.helpBoxes[it].imgDescription,
                    text = helpViewModel.getLocalizedTextUseCase(
                        helpViewModel.helpBoxes[it].text
                    )
                )
            }
        }
    }
}

@Composable
fun HelpListItem(
    img: Int,
    imgDescription: String,
    text: String,
) {
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(data = img)
            .scale(scale = Scale.FIT)
            .build()
    )
    CommonListItem(
        height = 160.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(52.dp),
            ) {
                Image(
                    painter = painter,
                    contentDescription = imgDescription,
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = TypographyCondenced.body1,
                modifier = Modifier.padding(16.dp)

            )
        }
    }
}