package com.valerytimofeev.h3pand.data.local.additional_data

import androidx.compose.ui.graphics.Color
import com.valerytimofeev.h3pand.R
import com.valerytimofeev.h3pand.ui.theme.*

/**
 * Data presets for each castle
 * @param castleName name of town (with localization)
 * @param img link to image resource (png)
 * @param sheetColor link to compose color
 */
enum class CastleSettings(
    val id: Int,
    val castleName: TextWithLocalization,
    val img: Int,
    val sheetColor: Color
) {
    CAS(1, TextWithLocalization("Castle", "Замок"), R.drawable.town_castle, CastleColor),
    RAM(2, TextWithLocalization("Rampart", "Оплот"), R.drawable.town_rampart, RampartColor),
    TOW(3, TextWithLocalization("Tower", "Башня"), R.drawable.town_tower, TowerColor),
    INF(4, TextWithLocalization("Inferno", "Инферно"), R.drawable.town_inferno, InfernoColor),
    NEC(5, TextWithLocalization("Necropolis", "Некрополис"), R.drawable.town_necropolis, NecropolisColor),
    DUN(6, TextWithLocalization("Dungeon", "Темница"), R.drawable.town_dungeon, DungeonColor),
    STR(7, TextWithLocalization("Stronghold", "Цитадель"), R.drawable.town_stronghold, StrongholdColor),
    FOR(8, TextWithLocalization("Fortress", "Крепость"), R.drawable.town_fortress, FortressColor),
    CON(9, TextWithLocalization("Conflux", "Сопряжение"), R.drawable.town_conflux, ConfluxColor),
    COV(10, TextWithLocalization("Cove", "Причал"), R.drawable.town_cove, CoveColor),
    FRG(11, TextWithLocalization("Forge", "Фабрика"), R.drawable.town_neutral, ForgeColor),
    NEU(0, TextWithLocalization("Neutral", "Нейтральная"), R.drawable.town_neutral, NeutralColor),
}

