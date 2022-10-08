package com.valerytimofeev.h3pand

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.valerytimofeev.h3pand.ui.mapselection.MapSelectionScreen
import com.valerytimofeev.h3pand.ui.pandcalculation.PandCalculationScreen
import com.valerytimofeev.h3pand.ui.theme.H3PandTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            H3PandTheme {
                //Navigation
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    NavHost(
                        navController = navController,
                        startDestination = "map_selection_screen"
                    ) {
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
                    }
                }
            }
        }
    }
}