package com.example.gethealth.ui.theme.screens.auth

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
import com.example.gethealth.model.User
import com.example.gethealth.data.UserRepository
import com.example.gethealth.ui.components.GetHealthButton
import com.example.gethealth.ui.components.GetHealthTextField

/**
 * The Register screen.
 *
 * Just like LoginScreen, this screen owns its own form state with
 * `remember { mutableStateOf(...) }` and reports success back up through a
 * callback (`onRegisterSuccess`) rather than navigating by itself.
 */
@Composable
fun RegisterScreen(
    onRegisterSuccess: (User) -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // RESPONSIVE LAYOUT: same pattern as LoginScreen — capped/centered
    // form width so it doesn't stretch full-width on a tablet.
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
                text = "Create Account",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            GetHealthTextField(
                value = name,
                onValueChange = { name = it; errorMessage = null },
                label = "Name"
            )

            Spacer(modifier = Modifier.height(12.dp))

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

            Spacer(modifier = Modifier.height(12.dp))

            GetHealthTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it; errorMessage = null },
                label = "Confirm Password",
                isPassword = true
            )

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(24.dp))

            GetHealthButton(text = "Register") {
                errorMessage = when {
                    name.isBlank() || email.isBlank() || password.isBlank() -> {
                        "Please fill in all fields."
                    }
                    password != confirmPassword -> {
                        "Passwords do not match."
                    }
                    else -> null
                }

                if (errorMessage == null) {
                    scope.launch {
                        try {
                            val user = UserRepository.register(name, email, password)
                            onRegisterSuccess(user)
                        } catch (e: Exception) {
                            errorMessage = e.message ?: "Registration failed."
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text("Already have an account? Login")
            }
        }
    }
}
