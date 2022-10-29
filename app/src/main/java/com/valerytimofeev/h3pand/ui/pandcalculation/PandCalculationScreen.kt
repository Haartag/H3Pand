package com.valerytimofeev.h3pand.ui.pandcalculation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.valerytimofeev.h3pand.domain.model.CastleSettings
import com.valerytimofeev.h3pand.ui.pandcalculation.dialog.DialogScreen
import com.valerytimofeev.h3pand.ui.pandcalculation.dialog.DialogViewModel
import com.valerytimofeev.h3pand.ui.pandcalculation.pandcalculationcomposables.ErrorBlock
import com.valerytimofeev.h3pand.ui.pandcalculation.pandcalculationcomposables.ItemsList
import com.valerytimofeev.h3pand.ui.pandcalculation.pandcalculationcomposables.SheetContent
import com.valerytimofeev.h3pand.ui.topbar.MainTopBar


@ExperimentalMaterialApi
@Composable
fun PandCalculationScreen(
    navController: NavController,
    mapName: String,
    viewModel: PandCalculationViewModel = hiltViewModel(),
    dialogViewModel: DialogViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    //System bar
    val systemUiController = rememberSystemUiController()
    systemUiController.setSystemBarsColor(
        color = CastleSettings.values()
            .find { it.id == viewModel.chosenCastleZone.value }?.sheetColor
            ?: MaterialTheme.colors.secondary
    )

    if (dialogViewModel.isDialogOpen()) {
        DialogScreen(
            header = { dialogViewModel.getDialogHeader()() },
            body = { dialogViewModel.getDialogBody()() }
        )
    }

    BottomSheetScaffold(

        //Bottom sheet content

        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //handle
                    Spacer(modifier = Modifier.height(21.dp))
                    Box(
                        modifier = Modifier
                            .height(4.dp)
                            .width(75.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(color = Color.Gray)
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    SheetContent(screenWidth = screenWidth)
                }
            }
        },
        sheetBackgroundColor = CastleSettings.values()
            .find { it.id == viewModel.chosenCastleZone.value }?.sheetColor
            ?: MaterialTheme.colors.secondary,
        sheetElevation = 16.dp,
        sheetShape = RoundedCornerShape(topEnd = 36.dp, topStart = 36.dp),
        sheetPeekHeight = viewModel.getSheetHeight(screenWidth)
    ) {

        //Screen content

        Box(modifier = Modifier.fillMaxSize()) {
            Column {
                MainTopBar(
                    buttonIcon = {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back button",
                        )
                    },
                    onButtonClicked = { navController.popBackStack() },
                    title = viewModel.fullMapName,
                    titleStyle = viewModel.mapNameTypo,
                    backgroundColor = CastleSettings.values()
                        .find { it.id == viewModel.chosenCastleZone.value }?.sheetColor
                        ?: MaterialTheme.colors.secondary
                )
                if (viewModel.isErrorShowed.value) {
                    ErrorBlock()
                }
                ItemsList(
                    screenHeight = screenHeight,
                    bottomSheetHeight = viewModel.getSheetHeight(screenWidth)
                )
            }
        }
    }
}
