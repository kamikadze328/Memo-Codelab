package com.kamikadze328.memo.core.ui.theme

import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val GreenPrimary = Color(0xFF9CCC65)
val GreenPrimaryDark = Color(0xFF6B9B37)
val OrangeAccent = Color(0xFFFF7043)

val LightColorPalette = lightColors(
    primary = GreenPrimary,
    primaryVariant = GreenPrimaryDark,
    onPrimary = Color.White,
    secondary = OrangeAccent,
    secondaryVariant = OrangeAccent,
    onSecondary = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    error = OrangeAccent,
    onError = Color.White,
)

val DarkColorPalette = LightColorPalette

