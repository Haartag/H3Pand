package com.valerytimofeev.h3pand.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.valerytimofeev.h3pand.R

val Overpass = FontFamily(
    Font(R.font.overpass_light, FontWeight.Light),
    Font(R.font.overpass_regular, FontWeight.Normal),
    Font(R.font.overpass_medium, FontWeight.Medium),
    Font(R.font.overpass_semibold, FontWeight.SemiBold),
    Font(R.font.overpass_bold, FontWeight.Bold),
)

val Typography = Typography(
    body1 = TextStyle(
        fontFamily = Overpass,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp
    ),
    body2 = TextStyle(
        fontFamily = Overpass,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = Overpass,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = Overpass,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp
    ),
    h4 = TextStyle(
        fontFamily = Overpass,
        fontWeight = FontWeight.Light,
        fontSize = 35.sp,
        letterSpacing = 0.25.sp
    ),
    h5 = TextStyle(
        fontFamily = Overpass,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    h6 = TextStyle(
        fontFamily = Overpass,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        letterSpacing = 0.20.sp
    ),
    caption = TextStyle(
        fontFamily = Overpass,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp
    ),
)