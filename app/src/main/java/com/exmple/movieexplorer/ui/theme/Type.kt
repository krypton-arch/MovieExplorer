package com.exmple.movieexplorer.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

private val fontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = emptyList()         // Works on real devices with GMS
)

private val outfitFont = GoogleFont("Outfit")
private val interFont = GoogleFont("Inter")

val OutfitFamily = FontFamily(
    Font(googleFont = outfitFont, fontProvider = fontProvider, weight = FontWeight.Light),
    Font(googleFont = outfitFont, fontProvider = fontProvider, weight = FontWeight.Normal),
    Font(googleFont = outfitFont, fontProvider = fontProvider, weight = FontWeight.Medium),
    Font(googleFont = outfitFont, fontProvider = fontProvider, weight = FontWeight.SemiBold),
    Font(googleFont = outfitFont, fontProvider = fontProvider, weight = FontWeight.Bold),
    Font(googleFont = outfitFont, fontProvider = fontProvider, weight = FontWeight.ExtraBold),
)

val InterFamily = FontFamily(
    Font(googleFont = interFont, fontProvider = fontProvider, weight = FontWeight.Normal),
    Font(googleFont = interFont, fontProvider = fontProvider, weight = FontWeight.Medium),
    Font(googleFont = interFont, fontProvider = fontProvider, weight = FontWeight.SemiBold),
)

val MovieTypography = Typography(
    // Hero / screen titles
    headlineLarge = TextStyle(
        fontFamily = OutfitFamily,
        fontSize = 30.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimary,
        letterSpacing = (-0.5).sp,
        lineHeight = 36.sp
    ),
    // Section headings
    headlineMedium = TextStyle(
        fontFamily = OutfitFamily,
        fontSize = 24.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextPrimary,
        letterSpacing = (-0.3).sp,
        lineHeight = 30.sp
    ),
    // Card titles
    titleLarge = TextStyle(
        fontFamily = OutfitFamily,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimary,
        letterSpacing = (-0.2).sp,
        lineHeight = 24.sp
    ),
    // Subtitles
    titleMedium = TextStyle(
        fontFamily = OutfitFamily,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        color = TextPrimary,
        letterSpacing = 0.sp,
        lineHeight = 20.sp
    ),
    titleSmall = TextStyle(
        fontFamily = OutfitFamily,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = TextSecondary,
        letterSpacing = 0.1.sp,
        lineHeight = 18.sp
    ),
    // Body text
    bodyLarge = TextStyle(
        fontFamily = InterFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        color = TextSecondary,
        letterSpacing = 0.1.sp,
        lineHeight = 20.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = InterFamily,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal,
        color = TextSecondary,
        letterSpacing = 0.1.sp,
        lineHeight = 18.sp
    ),
    bodySmall = TextStyle(
        fontFamily = InterFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        color = TextMuted,
        letterSpacing = 0.2.sp,
        lineHeight = 16.sp
    ),
    // Label / badges / chips
    labelLarge = TextStyle(
        fontFamily = OutfitFamily,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextPrimary,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontFamily = OutfitFamily,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextPrimary,
        letterSpacing = 0.3.sp
    ),
    labelSmall = TextStyle(
        fontFamily = OutfitFamily,
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium,
        color = TextMuted,
        letterSpacing = 0.4.sp
    )
)
