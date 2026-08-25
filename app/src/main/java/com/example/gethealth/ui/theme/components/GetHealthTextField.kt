package com.example.gethealth.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

/**
 * A reusable text field used for form inputs (email, password, name, etc.).
 *
 * What it is: Wraps Material 3's OutlinedTextField so every input field in
 *             the app has the same look, and screens don't need to repeat
 *             styling code.
 *
 * Why we need it: LoginScreen, RegisterScreen and future screens all need
 *             text inputs. Writing one reusable component avoids copy-pasted
 *             code and makes it easy to tweak all fields at once later.
 *
 * `isPassword` hides the typed text (useful for password fields).
 */
@Composable
fun GetHealthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (isPassword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = if (isPassword) {
            KeyboardOptions(keyboardType = KeyboardType.Password)
        } else {
            KeyboardOptions.Default
        },
        modifier = modifier.fillMaxWidth()
    )
}
