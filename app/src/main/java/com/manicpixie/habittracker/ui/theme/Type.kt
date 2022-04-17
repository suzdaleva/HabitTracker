package com.manicpixie.habittracker.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.manicpixie.habittracker.R


val neueMachinaFont = FontFamily(
    listOf(
        Font(R.font.neue_machina_light, FontWeight.Light),
        Font(R.font.neue_machina_regular, FontWeight.Normal),
        Font(R.font.neue_machina_ultrabold, FontWeight.ExtraBold),
    )
)
val ptSansFont = FontFamily(
    listOf(
        Font(R.font.pt_sans_regular, FontWeight.Normal),
    )
)
// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = ptSansFont,
        fontWeight = FontWeight.Normal,
        color = PrimaryBlack,
        letterSpacing = 0.02.em
    ),
    h3 = TextStyle(
        fontFamily = neueMachinaFont,
        fontWeight = FontWeight.Light,
        color = PrimaryBlack
    ),
    h4 = TextStyle(
        fontFamily = neueMachinaFont,
        fontWeight = FontWeight.Normal,
        color = PrimaryBlack
    ),
    button = TextStyle(
        fontFamily = neueMachinaFont,
        fontWeight = FontWeight.Normal
    ),
    caption = TextStyle(
        fontFamily = neueMachinaFont,
        fontWeight = FontWeight.ExtraBold,
        color = PrimaryBlack,
        lineHeight = 1.1.em
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)