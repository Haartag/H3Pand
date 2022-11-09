package com.valerytimofeev.h3pand.utils

import kotlin.math.ceil
import kotlin.math.roundToInt

//Math

fun Double.ceilToInt(): Int {
    return ceil(this).toInt()
}

fun Double.roundToTwoDigits(): Double {
    return (this * 100).roundToInt() / 100.0
}