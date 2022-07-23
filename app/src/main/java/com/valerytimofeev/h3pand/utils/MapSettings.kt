package com.valerytimofeev.h3pand.utils

enum class MapSettings(
    val mapName: String,
    val numberOfZones: Int,
    val valueRanges: List<IntRange>,
    val mirror: Boolean = false
) {
    /**
     * Value range must be over 2000
     */
    JC("Jebus Cross", 5, listOf(2000..22000, 10000..55000));

    companion object {
        fun getMapSettings(mapName: String) = values().find { it.name == mapName.uppercase() }
    }
}