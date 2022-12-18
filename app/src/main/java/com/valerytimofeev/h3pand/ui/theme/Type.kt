package com.valerytimofeev.h3pand.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.valerytimofeev.h3pand.R

val Ubuntu = FontFamily(
    Font(R.font.ubuntu_light, FontWeight.Light),
    Font(R.font.ubuntu_regular, FontWeight.Normal),
    Font(R.font.ubuntu_medium, FontWeight.Medium),
    Font(R.font.ubuntu_bold, FontWeight.Bold),
)
val UbuntuCondenced = FontFamily(
    Font(R.font.ubuntu_condensed, FontWeight.Normal),
)

val Typography = Typography(
    body1 = TextStyle(
        fontFamily = Ubuntu,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp
    ),
    body2 = TextStyle(
        fontFamily = Ubuntu,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = Ubuntu,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = Ubuntu,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp
    ),
    h4 = TextStyle(
        fontFamily = Ubuntu,
        fontWeight = FontWeight.Light,
        fontSize = 35.sp,
        letterSpacing = 0.25.sp
    ),
    h5 = TextStyle(
        fontFamily = Ubuntu,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp
    ),
    h6 = TextStyle(
        fontFamily = Ubuntu,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        letterSpacing = 0.20.sp
    ),
    caption = TextStyle(
        fontFamily = Ubuntu,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp
    ),
)

val TypographyCondenced = Typography(
    body1 = TextStyle(
        fontFamily = UbuntuCondenced,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp
    ),
    body2 = TextStyle(
        fontFamily = UbuntuCondenced,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = UbuntuCondenced,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = UbuntuCondenced,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp
    )
)

val Title = FontFamily(
    Font(R.font.quintessential, FontWeight.Normal),
)
val TitleTypo = Typography(
    h1 = TextStyle(
        fontFamily = Title,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp,
        letterSpacing = 0.3.sp
    )
)

val MapJc = FontFamily(
    Font(R.font.rye_regular, FontWeight.Normal),
)
val TypoJc = Typography(
    h4 = TextStyle(
        fontFamily = MapJc,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        letterSpacing = 0.25.sp
    ),
    h5 = TextStyle(
        fontFamily = MapJc,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        letterSpacing = 0.2.sp
    )
)

val MapMlyn = FontFamily(
    Font(R.font.berkshire, FontWeight.Normal),
)
val TypoMlyn = Typography(
    h4 = TextStyle(
        fontFamily = MapMlyn,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        letterSpacing = 0.25.sp
    ),
    h5 = TextStyle(
        fontFamily = MapMlyn,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        letterSpacing = 0.2.sp
    )
)
