package com.valerytimofeev.h3pand.data.additional_data

data class ZoneSettings(
    val zoneName: TextWithLocalization, val difficult: Int, val valueRange: IntRange
)

enum class MapSettings(
    val mapName: String,
    val numberOfZones: Int,
    val valueRanges: List<ZoneSettings>,
    val expRate: Int = 20,
    val goldRate: Int = 5,
    val spellRate: Int = 2,
    val unitRate: Int = 3,

    val mirror: Boolean = false
) {
    /**
     * Value range must be over 2000
     */
    JC(
        "Jebus Cross", 5, listOf(
            ZoneSettings(
                TextWithLocalization("Start", "Стартовая"),
                3,
                2000..22000
            ),
            ZoneSettings(
                TextWithLocalization("Central", "Центр"),
                5,
                10000..55000
            ),
        )
    );

    companion object {
        fun getMapSettings(mapName: String) = values().find { it.name == mapName.uppercase() }!!
    }
}