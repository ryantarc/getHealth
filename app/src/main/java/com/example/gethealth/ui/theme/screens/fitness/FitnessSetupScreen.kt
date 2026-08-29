package com.example.gethealth.ui.screens.fitness

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gethealth.data.model.Category
import com.example.gethealth.ui.components.GetHealthButton
import com.example.gethealth.ui.components.GetHealthTopBar


@Composable
fun FitnessSetupScreen(
    onWorkoutGenerated: () -> Unit,
    viewModel: FitnessViewModel = viewModel()
) {
    val configuration = LocalConfiguration.current
    val isWideScreen = configuration.screenWidthDp >= 600

    Scaffold(topBar = { GetHealthTopBar(title = "Fitness Advisor") }) { innerPadding ->
        if (isWideScreen) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IntroText()
                    BmiInputCard(viewModel)
                    BmiResultCard(viewModel)
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FocusAreaSection(viewModel)
                    ErrorText(viewModel)
                    GenerateButtonSection(viewModel, onWorkoutGenerated)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                IntroText()
                BmiInputCard(viewModel)
                BmiResultCard(viewModel)
                FocusAreaSection(viewModel)
                ErrorText(viewModel)
                Spacer(modifier = Modifier.height(4.dp))
                GenerateButtonSection(viewModel, onWorkoutGenerated)
            }
        }
    }
}

@Composable
private fun IntroText() {
    Text(
        text = "Calculate your BMI and get a tailored workout plan.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun BmiInputCard(viewModel: FitnessViewModel) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(text = "BMI Calculator", fontWeight = FontWeight.SemiBold)
            OutlinedTextField(
                value = viewModel.heightText,
                onValueChange = viewModel::onHeightChanged,
                label = { Text("Height (cm)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.weightText,
                onValueChange = viewModel::onWeightChanged,
                label = { Text("Weight (kg)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            GetHealthButton(text = "Calculate BMI") {
                viewModel.calculateBmi()
            }
        }
    }
}

@Composable
private fun BmiResultCard(viewModel: FitnessViewModel) {
    val bmi = viewModel.bmiResult ?: return
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "YOUR BMI", style = MaterialTheme.typography.labelMedium)
            Text(
                text = "${bmi.bmi}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${bmi.category.label} • Recommended: ${bmi.intensity.label}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun FocusAreaSection(viewModel: FitnessViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "Select Focus Area", fontWeight = FontWeight.SemiBold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Category.entries.forEach { category ->
                val selected = category in viewModel.selectedCategories
                FilterChip(
                    selected = selected,
                    onClick = { viewModel.toggleCategory(category) },
                    label = { Text(category.label) }
                )
            }
        }
    }
}

@Composable
private fun ErrorText(viewModel: FitnessViewModel) {
    viewModel.errorMessage?.let {
        Text(text = it, color = MaterialTheme.colorScheme.error)
    }
}

@Composable
private fun GenerateButtonSection(
    viewModel: FitnessViewModel,
    onWorkoutGenerated: () -> Unit
) {
    if (viewModel.isLoadingWorkout) {
        CircularProgressIndicator()
    } else {
        GetHealthButton(text = "Generate Workout") {
            viewModel.generateWorkout(
                onSuccess = onWorkoutGenerated,
                onError = viewModel::setError
            )
        }
    }
}