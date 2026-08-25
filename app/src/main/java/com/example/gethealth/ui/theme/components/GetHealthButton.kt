package com.example.gethealth.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A reusable primary button used across the whole app.
 *
 * What it is: A @Composable function is just a function that describes a
 *             piece of UI. Building our own "GetHealthButton" on top of
 *             Material 3's Button means every button in the app looks the
 *             same, and if we want to restyle all buttons later, we only
 *             change this one file.
 *
 * Where it's used: LoginScreen, RegisterScreen, DashboardScreen, etc.
 */
@Composable
fun GetHealthButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(text)
    }
}
