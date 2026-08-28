package com.example.gethealth.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import com.example.gethealth.data.UserRepository
import com.example.gethealth.ui.components.GetHealthButton
import com.example.gethealth.ui.components.GetHealthTextField

/**
 * The Login screen.
 *
 * State management: We use `remember { mutableStateOf(...) }` to hold the
 * text typed into each field. This is Compose's simplest way to keep track
 * of UI state — "remember" tells Compose to keep the value across redraws,
 * and "mutableStateOf" makes it something Compose can watch for changes.
 * This is intentionally simple for now; a real app might move this into a
 * ViewModel later, but that isn't needed for this foundation.
 *
 * `onLoginSuccess` is a callback (a function passed in from outside) that
 * this screen calls when login "succeeds". This screen doesn't know or care
 * *where* it navigates to — that logic lives in AppNavigation.kt. This
 * keeps navigation logic out of the screen itself, which is easier to
 * understand and change later.
 */
@Composable
fun LoginScreen(
    onLoginSuccess: (userName: String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // RESPONSIVE LAYOUT: the outer Box fills the whole screen and centers
    // its content. The inner Column is capped at 420dp wide, so on a
    // tablet the login form stays a comfortable, readable size in the
    // middle of the screen instead of stretching edge to edge.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 420.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "getHealth",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your personal health companion",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(32.dp))

            GetHealthTextField(
                value = email,
                onValueChange = { email = it; errorMessage = null },
                label = "Email"
            )

            Spacer(modifier = Modifier.height(12.dp))

            GetHealthTextField(
                value = password,
                onValueChange = { password = it; errorMessage = null },
                label = "Password",
                isPassword = true
            )

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Temporary validation — this is where real authentication will
            // eventually be plugged in (see UserRepository.login()).
            GetHealthButton(text = "Login") {
                if (email.isBlank() || password.isBlank()) {
                    errorMessage = "Please enter both email and password."
                } else {
                    scope.launch {
                        if (UserRepository.login(email, password)) {
                            onLoginSuccess(email.substringBefore("@"))
                        } else {
                            errorMessage = "Invalid email or password."
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Don't have an account?")
            TextButton(onClick = onNavigateToRegister) {
                Text("Register")
            }
        }
    }
}
