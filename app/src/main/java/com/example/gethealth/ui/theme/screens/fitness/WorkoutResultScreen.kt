package com.example.gethealth.ui.screens.fitness

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gethealth.domain.GeneratedWorkout
import com.example.gethealth.domain.WorkoutItem
import com.example.gethealth.ui.components.GetHealthButton
import com.example.gethealth.ui.components.GetHealthTopBar

@Composable
fun WorkoutResultScreen(
    onGenerateAnother: () -> Unit,
    viewModel: FitnessViewModel
) {
    val configuration = LocalConfiguration.current
    val isWideScreen = configuration.screenWidthDp >= 600
    val workout = viewModel.generatedWorkout

    Scaffold(topBar = { GetHealthTopBar(title = "Your Workout") }) { innerPadding ->
        if (workout == null) {
            Column(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(20.dp)) {
                Text("No workout generated yet.")
            }
            return@Scaffold
        }

        if (isWideScreen) {
            Row(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                Column(
                    modifier = Modifier
                        .weight(0.35f)
                        .fillMaxHeight()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SummaryCard(viewModel, workout)
                    GetHealthButton(text = "Generate Another Workout") {
                        viewModel.regenerateWorkout(onError = viewModel::setError)
                        onGenerateAnother()
                    }
                }
                ExerciseList(
                    workout = workout,
                    modifier = Modifier.weight(0.65f).fillMaxHeight(),
                    contentPadding = PaddingValues(end = 20.dp, top = 20.dp, bottom = 20.dp)
                )
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    SummaryCard(viewModel, workout)
                }
                ExerciseList(
                    workout = workout,
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp)
                )
                GetHealthButton(
                    text = "Generate Another Workout",
                    modifier = Modifier.padding(20.dp)
                ) {
                    viewModel.regenerateWorkout(onError = viewModel::setError)
                    onGenerateAnother()
                }
            }
        }
    }
}

@Composable
private fun SummaryCard(viewModel: FitnessViewModel, workout: GeneratedWorkout) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            val bmi = viewModel.bmiResult
            if (bmi != null) {
                Text(
                    text = "BMI ${bmi.bmi} • ${bmi.category.label}",
                    fontWeight = FontWeight.SemiBold
                )
            }
            Text(
                text = "Target: " + workout.selectedCategories.joinToString(", ") { it.label },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Intensity: ${workout.intensity.label}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ExerciseList(
    workout: GeneratedWorkout,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(workout.items) { item -> ExerciseCard(item) }
    }
}

@Composable
private fun ExerciseCard(item: WorkoutItem) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = item.exercise.name, fontWeight = FontWeight.SemiBold)
            Text(
                text = item.prescription,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = item.exercise.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}