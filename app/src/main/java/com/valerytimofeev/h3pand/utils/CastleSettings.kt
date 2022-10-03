package com.valerytimofeev.h3pand.utils

import androidx.compose.ui.graphics.Color
import com.valerytimofeev.h3pand.R
import com.valerytimofeev.h3pand.ui.theme.*

enum class CastleSettings(
    val id: Int,
    val castleName: String,
    val img: Int,
    val sheetColor: Color
) {
    CAS(1, "Castle", R.drawable.town_castle, CastleColor),
    CON(2, "Conflux", R.drawable.town_conflux, ConfluxColor),
    COV(3, "Cove", R.drawable.town_cove, CoveColor),
    DUN(4, "Dungeon", R.drawable.town_dungeon, DungeonColor),
    FOR(5, "Fortress", R.drawable.town_fortress, FortressColor),
    INF(6, "Inferno", R.drawable.town_inferno, InfernoColor),
    NEC(7, "Necropolis", R.drawable.town_necropolis, NecropolisColor),
    RAM(8, "Rampart", R.drawable.town_rampart, RampartColor),
    STR(9, "Stronghold", R.drawable.town_stronghold, StrongholdColor),
    TOW(10, "Tower", R.drawable.town_tower, TowerColor),
    NEU(0, "Neutral", R.drawable.town_neutral, NeutralColor),
}