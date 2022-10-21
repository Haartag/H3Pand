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
    CON(2, TextWithLocalization("Conflux", "Сопряжение"), R.drawable.town_conflux, ConfluxColor),
    COV(3, TextWithLocalization("Cove", "Причал"), R.drawable.town_cove, CoveColor),
    DUN(4, TextWithLocalization("Dungeon", "Темница"), R.drawable.town_dungeon, DungeonColor),
    FOR(5, TextWithLocalization("Fortress", "Крепость"), R.drawable.town_fortress, FortressColor),
    INF(6, TextWithLocalization("Inferno", "Инферно"), R.drawable.town_inferno, InfernoColor),
    NEC(7, TextWithLocalization("Necropolis", "Некрополис"), R.drawable.town_necropolis, NecropolisColor),
    RAM(8, TextWithLocalization("Rampart", "Оплот"), R.drawable.town_rampart, RampartColor),
    STR(9, TextWithLocalization("Stronghold", "Цитадель"), R.drawable.town_stronghold, StrongholdColor),
    TOW(10, TextWithLocalization("Tower", "Башня"), R.drawable.town_tower, TowerColor),
    NEU(0, TextWithLocalization("Neutral", "Нейтральная"), R.drawable.town_neutral, NeutralColor),
}

