package com.valerytimofeev.h3pand.data.local.additional_data

import androidx.compose.ui.text.TextStyle
import com.valerytimofeev.h3pand.R
import com.valerytimofeev.h3pand.ui.theme.TypoJc
import com.valerytimofeev.h3pand.ui.mapselection.MapSelectionScreen

/**
 * Each map has a set of zones with preset object generation parameters for each zone.
 * @param zoneName displayed zone name
 * @param difficult number of difficult set from [Difficult]
 * @param valueRange list of [ValueRange]
 */
data class ZoneSettings(
    val zoneName: TextWithLocalization,
    val difficult: Int,
    val valueRange: List<ValueRange>
)

/**
 * Each zone has up to three value ranges, each with its own frequency.
 * All parameters are taken from the map settings in the map editor.
 * @param number number of value range
 * @param range range of values from which items are selected for generation
 * @param frequency the frequency of this range
 */
data class ValueRange(
    val number: Int,
    val range: IntRange,
    val frequency: Int
)

/**
 * Set of constants for each map.
 *
 * @param mapName displayed map name
 * @param numberOfZones the number of zones on the map that have a central town
 * @param valueRanges list of [ZoneSettings] for each zone type
 * @param expRate frequency of exp box generation
 * @param goldRate frequency of gold box generation
 * @param spellRate frequency of spell box generation
 * @param unitRate frequency of unit box generation
 * @param tileImage resource with image for [MapSelectionScreen]
 * @param tileTypo typography for [MapSelectionScreen]
 */
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
    ),
    JCL(
        mapName = "Jebus Cross L",
        numberOfZones = 5,
        valueRanges = listOf(
            ZoneSettings(
                TextWithLocalization("Start", "Стартовая"),
                2,
                listOf(
                    ValueRange(0, 300..3000, 14),
                    ValueRange(1, 5000..16000, 6),
                    ValueRange(2, 12000..22000, 1),
                )
            ),
            ZoneSettings(
                TextWithLocalization("Central", "Центр"),
                4,
                listOf(
                    ValueRange(0, 10000..25000, 10),
                    ValueRange(1, 25000..35000, 10),
                    ValueRange(2, 35000..55000, 3),
                )
            ),
        ),
        tileImage = R.drawable.map_test,
        tileTypo = TypoJc.h4
    );

    companion object {
        fun getMapSettings(mapName: String) = values().find { it.name == mapName.uppercase() }!!
    }
}