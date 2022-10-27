package com.valerytimofeev.h3pand.ui.mapselection

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.valerytimofeev.h3pand.ui.theme.UnitColor
import com.valerytimofeev.h3pand.ui.topbar.MainTopBar
import com.valerytimofeev.h3pand.ui.util_composables.BackPressHandler
import com.valerytimofeev.h3pand.R

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
        MainTopBar(
            title = viewModel.mapTitle,
            buttonIcon = ImageVector.vectorResource(id = R.drawable.ic_icon),
            onButtonClicked = {}
        ) {
            Icon(painterResource(id = R.drawable.ic_icon), contentDescription = "")
        }
        MapList(
            modifier = Modifier.weight(1f),
            navController
        )
        BottomButtonRow(navController)
    }
}

@Composable
fun MapList(
    modifier: Modifier,
    navController: NavController,
    viewModel: MapSelectionViewModel = hiltViewModel()
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        modifier = modifier
    ) {
        items(count = viewModel.maps.size) {
            MapItem(
                mapName = viewModel.maps[it].mapName,
                mapId = viewModel.maps[it].name,
                navController = navController,
                mapImg = viewModel.maps[it].tileImage,
                mapTypo = viewModel.maps[it].tileTypo
            )
        }
    }
}

@Composable
fun MapItem(
    mapName: String,
    mapId: String,
    navController: NavController,
    mapImg: Int,
    mapTypo: TextStyle
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .shadow(elevation = 4.dp, RoundedCornerShape(4.dp))
            .fillMaxWidth()
            .background(UnitColor)
            .height(88.dp)
            .clickable {
                navController.navigate("pand_calculation_screen/${mapId}")
            },
        contentAlignment = Alignment.CenterStart
    ) {
        val painter = rememberAsyncImagePainter(
            ImageRequest.Builder(LocalContext.current)
                .data(data = mapImg)
                .build()
        )
        Row() {
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painter,
                contentDescription = mapName,
                modifier = Modifier,
            )
        }
        Text(
            text = mapName,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            style = mapTypo
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}
