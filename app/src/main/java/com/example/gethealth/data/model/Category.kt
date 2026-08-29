package com.example.gethealth.data.model

enum class Category(val dbValue: String, val label: String) {
    CARDIO("CARDIO", "Cardio"),
    LEGS("LEGS", "Legs"),
    ARMS("ARMS", "Arms"),
    ABS("ABS", "Abs");

    companion object {
        fun fromDbValue(value: String): Category? =
            entries.firstOrNull { it.dbValue.equals(value.trim(), ignoreCase = true) }
    }
}