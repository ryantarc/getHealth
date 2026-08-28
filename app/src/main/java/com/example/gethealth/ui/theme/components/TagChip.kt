package com.example.gethealth.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * A small rounded "pill" label, used for recipe tags like "High Protein",
 * "Quick", "Low Carb" (see the Meal Planner screens in the design deck).
 *
 * Why we need it: These little tag pills show up repeatedly on every
 * recipe card, so pulling them into one reusable component avoids
 * repeating the same Box + shape + padding code everywhere.
 */
@Composable
fun TagChip(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}


