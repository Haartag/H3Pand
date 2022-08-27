package com.valerytimofeev.h3pand.utils

import androidx.compose.ui.graphics.Color
import com.valerytimofeev.h3pand.R
import com.valerytimofeev.h3pand.ui.theme.Secondary

enum class CastleSettings(
    val id: Int,
    val castleName: String,
    val img: Int,
    val sheetColor: Color
) {
    CAS(1, "Castle", R.drawable.ic_castle, Secondary),
    CON(2, "Conflux", R.drawable.ic_placeholder, Secondary),
    COV(3, "Cove", R.drawable.ic_placeholder, Secondary),
    DUN(4, "Dungeon", R.drawable.ic_placeholder, Secondary),
    FOR(5, "Fortress", R.drawable.ic_placeholder, Secondary),
    INF(6, "Inferno", R.drawable.ic_placeholder, Color.LightGray),
    NEC(7, "Necropolis", R.drawable.ic_placeholder, Secondary),
    RAM(8, "Rampart", R.drawable.ic_rampart, Color.Green),
    STR(9, "Stronghold", R.drawable.ic_placeholder, Secondary),
    TOW(10, "Tower", R.drawable.ic_tower, Secondary),
    NEU(0, "Neutral", R.drawable.ic_placeholder, Secondary),
}