package com.valerytimofeev.h3pand.data.local.additional_data

/**
 * Coefficient presets for map zones
 */
enum class Difficult(
    val minValue1: Int,
    val coefficient1: Double,
    val minValue2: Int,
    val coefficient2: Double
) {
    ONE(2500, 0.5, 7500, 0.5),
    TWO(1500, 0.75, 7500, 0.75),
    THREE(1000, 1.0, 7500, 1.0),
    FOUR(500, 1.5, 5000, 1.0),
    FIVE(0, 1.5, 5000, 1.5);

    companion object {
        fun getDifficultForZone(zoneLvl: Int) = when (zoneLvl) {
            1 -> ONE
            2 -> TWO
            3 -> THREE
            4 -> FOUR
            5 -> FIVE
            else -> null
        }
    }
}