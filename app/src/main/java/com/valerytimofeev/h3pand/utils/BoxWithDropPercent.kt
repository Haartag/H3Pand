package com.valerytimofeev.h3pand.utils

/**
 * Data class for GetBoxWithPercentUseCase output.
 */
data class BoxWithDropPercent(
    val name: String,
    var dropChance: Double,
    val mostLikelyGuard: Int,
    val range: IntRange,
    val img: String,
)
