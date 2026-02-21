package com.valerytimofeev.h3pand.ui.pandcalculation

import android.app.Activity
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.valerytimofeev.h3pand.data.local.additional_data.CastleSettings
import com.valerytimofeev.h3pand.ui.pandcalculation.dialog.DialogScreen
import com.valerytimofeev.h3pand.ui.pandcalculation.dialog.DialogViewModel
import com.valerytimofeev.h3pand.ui.pandcalculation.dialog.SpecifyDialog
import com.valerytimofeev.h3pand.ui.pandcalculation.pandcalculationcomposables.ErrorBlock
import com.valerytimofeev.h3pand.ui.pandcalculation.pandcalculationcomposables.ItemsListWithGroups
import com.valerytimofeev.h3pand.ui.pandcalculation.pandcalculationcomposables.ItemsListWithoutGroups
import com.valerytimofeev.h3pand.ui.pandcalculation.pandcalculationcomposables.SheetContent
import com.valerytimofeev.h3pand.ui.topbar.MainTopBar
import dev.chrisbanes.snapper.ExperimentalSnapperApi


@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalSnapperApi
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
    val castleColor = CastleSettings.values()
        .find { it.id == viewModel.chosenCastleZone.value }?.sheetColor
        ?: MaterialTheme.colors.secondary

    val view = LocalView.current
    SideEffect {
        val window = (view.context as Activity).window
        WindowInsetsControllerCompat(window, view).apply {
            // Adjust based on whether castleColor is light or dark
            isAppearanceLightStatusBars = castleColor.luminance() > 0.5f
            isAppearanceLightNavigationBars = false
        }
    }

    if (dialogViewModel.isDialogOpen()) {
        DialogScreen(
            header = { dialogViewModel.getDialogHeader()() },
            body = { dialogViewModel.getDialogBody()() }
        )
    }
    if (viewModel.isSpecifyDialogShown.value) {
        SpecifyDialog(
            onConfirm = {
                viewModel.setChosenGuardRange(
                    viewModel.exactlyGuardianNumber.value + 1
                            ..viewModel.exactlyGuardianNumber.value + 1
                )
                viewModel.closeSpecifyDialog()
                viewModel.getBoxesList()
            },
            onDismiss = { viewModel.closeSpecifyDialog() }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(castleColor)
            .navigationBarsPadding()
    ) {
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
            sheetBackgroundColor = castleColor,
            sheetElevation = 16.dp,
            //sheetShape = RoundedCornerShape(topEnd = 36.dp, topStart = 36.dp),
            sheetPeekHeight = viewModel.getSheetHeight(screenWidth)
        ) {

            //Screen content

            Box(modifier = Modifier.fillMaxSize()) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding() //pushes content below status bar
                ) {
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
                        backgroundColor = castleColor
                    )
                    if (viewModel.isErrorShowed.value) {
                        ErrorBlock()
                    }
                    if (viewModel.isGroup) {
                        ItemsListWithGroups(
                            screenHeight = screenHeight,
                            bottomSheetHeight = viewModel.getSheetHeight(screenWidth)
                        )
                    } else {
                        ItemsListWithoutGroups(
                            screenHeight = screenHeight,
                            bottomSheetHeight = viewModel.getSheetHeight(screenWidth)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsTopHeight(WindowInsets.statusBars)
                        .background(castleColor)
                )
            }
        }
    }
}
