package com.valerytimofeev.h3pand.data.additional_data

import androidx.compose.ui.text.TextStyle
import com.valerytimofeev.h3pand.R
import com.valerytimofeev.h3pand.ui.theme.TypoJc

data class ZoneSettings(
    val zoneName: TextWithLocalization, val difficult: Int, val valueRange: List<ValueRange>
)

data class ValueRange(
    val type: Int,
    val range: IntRange,
    val frequence: Int
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
                listOf(
                    ValueRange(0, 300..3000, 14),
                    ValueRange(1, 5000..16000, 6),
                    ValueRange(2, 12000..22000, 1),
                )
            ),
            ZoneSettings(
                TextWithLocalization("Central", "Центр"),
                5,
                listOf(
                    ValueRange(0, 10000..25000, 10),
                    ValueRange(1, 25000..35000, 10),
                    ValueRange(2, 35000..55000, 3),
                )
            ),
        ),
        tileImage = R.drawable.map_jc_cropped,
        tileTypo = TypoJc.h4
    );

    companion object {
        fun getMapSettings(mapName: String) = values().find { it.name == mapName.uppercase() }!!
    }
}