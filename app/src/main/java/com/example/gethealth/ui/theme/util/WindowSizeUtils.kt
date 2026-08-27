package com.example.gethealth.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration

/**
 * A simple description of how much horizontal space is available.
 *
 * What it is: A small enum instead of Google's full WindowSizeClass API
 * (which needs an extra dependency). This is enough to make "does this
 * look like a phone or a tablet?" decisions across the app.
 *
 * Why we need it: We want screens to reflow — a single column on a phone,
 * multiple columns / a side rail on a tablet — without maintaining two
 * separate copies of every screen. Each screen just checks this once and
 * adjusts its layout.
 *
 * The 600dp breakpoint matches Google's own guidance for "this is roughly
 * tablet-sized," which is why it shows up throughout Android documentation.
 */
enum class WindowWidthSize { COMPACT, EXPANDED }

@Composable
@ReadOnlyComposable
fun rememberWindowWidthSize(): WindowWidthSize {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    return if (screenWidthDp >= 600) WindowWidthSize.EXPANDED else WindowWidthSize.COMPACT
}
