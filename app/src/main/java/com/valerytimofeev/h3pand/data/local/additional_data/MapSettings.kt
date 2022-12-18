package com.valerytimofeev.h3pand.data.local.additional_data

import androidx.compose.ui.text.TextStyle
import com.valerytimofeev.h3pand.R
import com.valerytimofeev.h3pand.data.local.database.BoxValueItem
import com.valerytimofeev.h3pand.ui.theme.TypoJc
import com.valerytimofeev.h3pand.ui.mapselection.MapSelectionScreen
import com.valerytimofeev.h3pand.ui.theme.TypoMlyn
import com.valerytimofeev.h3pand.ui.theme.Typography

/**
 * Each map has a set of zones with preset object generation parameters for each zone.
 * @param zoneName displayed zone name
 * @param difficult number of difficult set from [Difficult]
 * @param valueRange list of [ValueRange]
 * @param zoneRestrictedBoxes forbidden boxes of this zone
 */
data class ZoneSettings(
    val zoneName: TextWithLocalization,
    val difficult: Int,
    val valueRange: List<ValueRange>,
    val zoneRestrictedBoxes: BoxRestrictions? = null,
    val zoneModifiedAddValues: ModifiedAddValues? = null
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
 * Some maps may have forbidden boxes (by type or just some boxes)
 * @param restrictedTypes list of banned types
 * @param restrictedIds list of banned Ids
 */
data class BoxRestrictions(
    val restrictedTypes: List<String>?,
    val restrictedIds: List<Int>?,
    val newBoxes: List<BoxValueItem>?
)

/**
 * Some maps may have a modified value of additional items
 * @param modifiedAddValues
 */
data class ModifiedAddValues(
    val modifiedAddValues: Map<Int, Int>?
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
    val minOfZones: Int = 1,
    val stepOfZones: Int = 1,
    val guaranteedNotMainZones: Int = 1,
    val valueRanges: List<ZoneSettings>,
    val expRate: Int = 20,
    val goldRate: Int = 5,
    val spellRate: Int = 2,
    val unitRate: Int = 3,
    val flatCalculation: Boolean = false,
    val boxRestrictions: BoxRestrictions? = null,
    val modifiedAddValues: ModifiedAddValues? = null,
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
    ),
    JO(
        mapName = "Jebus outcast",
        numberOfZones = 5,
        valueRanges = listOf(
            ZoneSettings(
                TextWithLocalization("Start", "Стартовая"),
                3,
                listOf(
                    ValueRange(0, 100..5000, 6),
                    ValueRange(1, 5000..15000, 10),
                    ValueRange(2, 15000..22000, 3),
                ),
                zoneRestrictedBoxes = BoxRestrictions(
                    null,
                    listOf(1093, 1067),
                    null
                ),
                zoneModifiedAddValues = ModifiedAddValues(
                    mapOf(
                        87 to 3000, 83 to 3000, 64 to 500, 56 to 500, 57 to 500, 60 to 500, 61 to 500,
                        62 to 500, 63 to 500, 84 to 1500, 29 to 1500, 89 to 3000, 69 to 2000, 53 to 1000,
                        80 to 1000, 88 to 2000
                    )
                ),
            ),
            ZoneSettings(
                TextWithLocalization("Central", "Центр"),
                5,
                listOf(
                    ValueRange(0, 10000..20000, 3),
                    ValueRange(1, 20000..40000, 10),
                    ValueRange(2, 40000..60000, 7),
                ),
                zoneRestrictedBoxes = BoxRestrictions(
                    null,
                    null,
                    listOf(
                        BoxValueItem(
                            18,
                            "All spells",
                            "Все заклинания",
                            55000,
                            "spells3",
                            "Spell"
                        )
                    )
                ),
                zoneModifiedAddValues = ModifiedAddValues(
                    mapOf(16 to 50000)
                ),
            ),
        ),
        boxRestrictions = BoxRestrictions(
            listOf("Exp", "Gold", "Spell"),
            null,
            null
        ),
        modifiedAddValues = ModifiedAddValues(
            mapOf(68 to 2000, 81 to 1500)
        ),
        tileImage = R.drawable.map_test,
        tileTypo = Typography.h4
    ),
    MLYN(
        mapName = "Mlyn 1.7c",
        numberOfZones = 10,
        guaranteedNotMainZones = 8,
        minOfZones = 2,
        valueRanges = listOf(
            ZoneSettings(
                TextWithLocalization("Start", "Стартовая"),
                3,
                listOf(
                    ValueRange(0, 500..3000, 99),
                    ValueRange(1, 3500..12000, 99),
                    //ValueRange(2, 15540..15550, 99),
                    ValueRange(2, 15540..17500, 99),
                ),
                zoneRestrictedBoxes = BoxRestrictions(
                    listOf("Exp", "Gold", "Spell"),
                    listOf(
                        1002, 1004, 1006, 1008, 1009, 1010, 1011, 1012, 1013, 1014,
                        1016, 1018, 1020, 1022, 1023, 1024, 1025, 1026, 1027, 1028,
                        1030, 1032, 1034, 1035, 1037, 1038, 1039, 1040, 1041, 1042, 1043,
                        1045, 1047, 1049, 1051, 1052, 1053, 1054, 1055, 1056, 1057,
                        1059, 1061, 1063, 1065, 1066, 1067, 1068, 1069, 1070, 1071,
                        1073, 1075, 1077, 1079, 1080, 1081, 1082, 1083, 1084, 1085,
                        1087, 1089, 1091, 1093, 1094, 1095, 1096, 1097, 1098, 1099,
                        1101, 1103, 1105, 1107, 1108, 1109, 1110, 1111, 1112, 1113,
                        1115, 1117, 1119, 1121, 1122, 1123, 1124, 1125, 1126, 1127,
                        1129, 1131, 1133, 1135, 1136, 1137, 1138, 1139, 1140, 1141
                    ),
                    null
                ),
            ),
/*            ZoneSettings(
                TextWithLocalization("Central", "Центр"),
                5,
                listOf(
                    ValueRange(0, 10000..20000, 3),
                    ValueRange(1, 20000..40000, 10),
                    ValueRange(2, 40000..60000, 7),
                )
            ),*/
        ),
        flatCalculation = true,
        tileImage = R.drawable.map_test,
        tileTypo = TypoMlyn.h4
    );

    companion object {
        fun getMapSettings(mapName: String) = values().find { it.name == mapName.uppercase() }!!
    }
}