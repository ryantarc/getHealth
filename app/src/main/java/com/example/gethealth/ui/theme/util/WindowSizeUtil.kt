package com.example.gethealth.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

/**
 * A simple, dependency-free way to tell whether we're on a phone-sized or
 * tablet-sized screen, so screens can rearrange themselves instead of just
 * stretching a phone layout wider.
 *
 * What it is: We read the current screen width in dp (density-independent
 * pixels) from LocalConfiguration and bucket it into three sizes, mirroring
 * Google's own breakpoints for "compact / medium / expanded" window sizes:
 *   - COMPACT: phones (most phones in portrait)
 *   - MEDIUM:  small tablets, phones in landscape, foldables
 *   - EXPANDED: large tablets, most tablets in landscape
 *
 * Why not use Google's official `WindowSizeClass` library: it requires an
 * extra Gradle dependency and an Activity-level API. For a foundation
 * template, reading screenWidthDp directly is simpler for beginners to
 * understand and has zero extra setup — the team can always upgrade to the
 * official library later without changing how any screen uses this.
 */
enum class WindowSize {
    COMPACT,
    MEDIUM,
    EXPANDED
}

@Composable
fun rememberWindowSize(): WindowSize {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    return when {
        screenWidthDp < 600 -> WindowSize.COMPACT
        screenWidthDp < 840 -> WindowSize.MEDIUM
        else -> WindowSize.EXPANDED
    }
}
