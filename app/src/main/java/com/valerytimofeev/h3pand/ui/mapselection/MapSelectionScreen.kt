package com.valerytimofeev.h3pand.ui.mapselection

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.valerytimofeev.h3pand.ui.pandcalculation.util_composables.BackPressHandler
import com.valerytimofeev.h3pand.ui.theme.MapItemBackground
import com.valerytimofeev.h3pand.ui.theme.MapItemGradient

@Composable
fun MapSelectionScreen(
    navController: NavController,
    viewModel: MapSelectionViewModel = hiltViewModel()
) {
    //Close app to prevent navigation to splashscreen
    val activity = (LocalContext.current as? Activity)
    BackPressHandler(
       onBackPressed = {
           activity?.finish()
       }
    )
    Column() {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .weight(1f)
        ) {
            Text(
                text = viewModel.mapTitle,
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
            )
            MapList(navController)
        }
        BottomButtonRow(navController)
    }
}

@Composable
fun MapList(
    navController: NavController,
    viewModel: MapSelectionViewModel = hiltViewModel()
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp)
    ) {
        items(count = viewModel.maps.size) {
            MapItem(
                mapName = viewModel.maps[it].mapName,
                mapId = viewModel.maps[it].name,
                navController
            )
        }
    }
}

@Composable
fun MapItem(
    mapName: String,
    mapId: String,
    navController: NavController,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .shadow(elevation = 4.dp, RoundedCornerShape(4.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        MapItemBackground,
                        MapItemGradient
                    )
                )
            )
            .fillMaxWidth()
            .height(78.dp)
            .padding(8.dp)
            .clickable {
                navController.navigate("pand_calculation_screen/${mapId}")
            }
    ) {
        Text(
            text = mapName,
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}
