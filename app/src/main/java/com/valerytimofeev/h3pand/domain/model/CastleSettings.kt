package com.valerytimofeev.h3pand.domain.model

import androidx.compose.ui.graphics.Color
import com.valerytimofeev.h3pand.R
import com.valerytimofeev.h3pand.data.additional_data.TextWithLocalization
import com.valerytimofeev.h3pand.ui.theme.*

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
    NEU(0, TextWithLocalization("Neutral", "Нейтральная"), R.drawable.town_neutral, NeutralColor),
}

