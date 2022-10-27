package com.valerytimofeev.h3pand.data.additional_data

import androidx.compose.ui.text.TextStyle
import com.valerytimofeev.h3pand.R
import com.valerytimofeev.h3pand.ui.theme.TypoJc

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
    val mirror: Boolean = false,
    val tileImage: Int,
    val tileTypo: TextStyle
) {
    /**
     * Value range must be over 2000
     */
    JC(
        mapName = "Jebus Cross",
        numberOfZones = 5,
        valueRanges = listOf(
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
        ),
        tileImage = R.drawable.map_jc_cropped,
        tileTypo = TypoJc.h4
    );

    companion object {
        fun getMapSettings(mapName: String) = values().find { it.name == mapName.uppercase() }!!
    }
}