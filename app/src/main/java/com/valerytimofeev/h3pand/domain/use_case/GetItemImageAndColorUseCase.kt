package com.valerytimofeev.h3pand.domain.use_case

import androidx.compose.ui.graphics.Color
import com.valerytimofeev.h3pand.R
import com.valerytimofeev.h3pand.ui.theme.ExpColor
import com.valerytimofeev.h3pand.ui.theme.GoldColor
import com.valerytimofeev.h3pand.ui.theme.SpellColor
import javax.inject.Inject

class GetItemImageAndColorUseCase (
    val itemImage: String
) {
    fun getItemImage(): Int {
        return when (itemImage) {
            "gold" -> R.drawable.item_gold
            "exp" -> R.drawable.item_exp
            "spells1" -> R.drawable.item_spells1
            "spells2" -> R.drawable.item_spells2
            "spells3" -> R.drawable.item_spells3
            "air" -> R.drawable.item_air
            "earth" -> R.drawable.item_earth
            "fire" -> R.drawable.item_fire
            "water" -> R.drawable.item_water
            else -> R.drawable.placeholder
        }
    }
    fun getItemColor(): Color {
        return when (itemImage) {
            "gold" -> GoldColor
            "exp" -> ExpColor
            "spells1" -> SpellColor
            "spells2" -> SpellColor
            "spells3" -> SpellColor
            "air" -> SpellColor
            "earth" -> SpellColor
            "fire" -> SpellColor
            "water" -> SpellColor
            else -> Color.White
        }
    }
}

