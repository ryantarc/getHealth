package com.example.gethealth.ui.screens.fitness

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

private object FitnessRoutes {
    const val SETUP = "fitness/setup"
    const val RESULT = "fitness/result"
}

@Composable
fun FitnessScreen() {
    val navController = rememberNavController()
    val sharedViewModel: FitnessViewModel = viewModel()

    NavHost(navController = navController, startDestination = FitnessRoutes.SETUP) {
        composable(FitnessRoutes.SETUP) {
            FitnessSetupScreen(
                viewModel = sharedViewModel,
                onWorkoutGenerated = { navController.navigate(FitnessRoutes.RESULT) }
            )
        }
        composable(FitnessRoutes.RESULT) {
            WorkoutResultScreen(
                viewModel = sharedViewModel,
                onGenerateAnother = { navController.popBackStack() }   // ← fixed: goes back to Setup
            )
        }
    }
}