package com.valerytimofeev.h3pand.domain.use_case

import androidx.compose.ui.graphics.Color
import com.valerytimofeev.h3pand.R
import com.valerytimofeev.h3pand.ui.theme.ExpColor
import com.valerytimofeev.h3pand.ui.theme.GoldColor
import com.valerytimofeev.h3pand.ui.theme.SpellColor
import com.valerytimofeev.h3pand.ui.theme.UnitColor

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
            "Pawn" -> R.drawable.item_pawn
            "Knight" -> R.drawable.item_knight
            "Rook" -> R.drawable.item_rook
            "Queen" -> R.drawable.item_queen
            "King" -> R.drawable.item_king
            else -> R.drawable.ic_dice
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
            "Pawn" -> UnitColor
            "Knight" -> UnitColor
            "Rook" -> UnitColor
            "Queen" -> UnitColor
            "King" -> UnitColor
            else -> Color.White
        }
    }

    fun getAddValueImage(): Int{
        return when (itemImage) {
            "Custom" -> R.drawable.av_custom
            "Misc." -> R.drawable.av_misc
            "Resource" -> R.drawable.av_resource
            "Bank" -> R.drawable.av_bank
            "Artifact" -> R.drawable.av_artifact
            "Dwelling" -> R.drawable.av_dwelling
            else -> R.drawable.ic_question
        }
    }
}

