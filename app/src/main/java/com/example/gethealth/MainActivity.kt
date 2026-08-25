package com.example.gethealth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.gethealth.navigation.AppNavigation
import com.example.gethealth.ui.theme.GetHealthTheme

/**
 * The single Activity that hosts the entire app.
 * Why it's this small: with Jetpack Compose + Navigation Compose, we don't
 * need multiple Activities/Fragments. MainActivity's only job is to apply
 * the app theme and start the navigation graph (AppNavigation). All actual
 * screens live in their own files under ui/screens — nothing about Login,
 * Dashboard, etc. belongs here.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GetHealthTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation()
                }
            }
        }
    }
}
