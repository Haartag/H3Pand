package com.valerytimofeev.h3pand.domain.use_case

import androidx.compose.ui.graphics.Color
import com.valerytimofeev.h3pand.R
import com.valerytimofeev.h3pand.ui.theme.*

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
    fun getItemColor(isLight: Boolean = false): Color {
        return when (itemImage) {
            "gold" -> if (isLight) GoldColorLight else GoldColor
            "exp" -> if (isLight) ExpColorLight else ExpColor
            "spells1" -> if (isLight) SpellColorLight else SpellColor
            "spells2" -> if (isLight) SpellColorLight else SpellColor
            "spells3" -> if (isLight) SpellColorLight else SpellColor
            "air" -> if (isLight) SpellColorLight else SpellColor
            "earth" -> if (isLight) SpellColorLight else SpellColor
            "fire" -> if (isLight) SpellColorLight else SpellColor
            "water" -> if (isLight) SpellColorLight else SpellColor
            "Pawn" -> if (isLight) UnitColorLight else UnitColor
            "Knight" -> if (isLight) UnitColorLight else UnitColor
            "Rook" -> if (isLight) UnitColorLight else UnitColor
            "Queen" -> if (isLight) UnitColorLight else UnitColor
            "King" -> if (isLight) UnitColorLight else UnitColor
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

