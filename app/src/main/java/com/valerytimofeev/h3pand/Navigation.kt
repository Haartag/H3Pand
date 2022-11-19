package com.valerytimofeev.h3pand

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.valerytimofeev.h3pand.ui.about.AboutLicensesScreen
import com.valerytimofeev.h3pand.ui.about.AboutScreen
import com.valerytimofeev.h3pand.ui.contact.ContactScreen
import com.valerytimofeev.h3pand.ui.help.HelpScreen
import com.valerytimofeev.h3pand.ui.mapselection.MapSelectionScreen
import com.valerytimofeev.h3pand.ui.pandcalculation.PandCalculationScreen
import com.valerytimofeev.h3pand.ui.settings.SettingsScreen
import com.valerytimofeev.h3pand.ui.splashscreen.SplashScreen
import dev.chrisbanes.snapper.ExperimentalSnapperApi

@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalSnapperApi
@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash_screen"
    ) {
        composable("splash_screen") {
            SplashScreen(navController = navController)
        }
        composable("map_selection_screen") {
            MapSelectionScreen(navController = navController)
        }
        composable("pand_calculation_screen/{mapName}", arguments = listOf(
            navArgument("mapName") {
                type = NavType.StringType
            }
        )
        ) {
            val mapName = remember {
                it.arguments?.getString("mapName")
            }
            PandCalculationScreen(
                navController = navController,
                mapName = mapName ?: ""
            )
        }
        composable("settings_screen") {
            SettingsScreen(navController = navController)
        }
        composable("about_screen") {
            AboutScreen(navController = navController)
        }
        composable("about_licenses_screen") {
            AboutLicensesScreen(navController = navController)
        }
        composable("help_screen") {
            HelpScreen(navController = navController)
        }
        composable("contact_screen") {
            ContactScreen(navController = navController)
        }
    }

}