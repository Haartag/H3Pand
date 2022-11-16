package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.data.local.database.AdditionalValueItem
import com.valerytimofeev.h3pand.data.local.database.Dwelling
import com.valerytimofeev.h3pand.data.local.database.Guard
import com.valerytimofeev.h3pand.domain.model.CurrentLocal
import com.valerytimofeev.h3pand.data.local.additional_data.TextWithLocalization
import com.valerytimofeev.h3pand.domain.model.SearchItem

class GetLocalizedTextUseCase {
    operator fun invoke(
        text: TextWithLocalization
    ): String {
        val currentLocal = CurrentLocal.local

        return text.takeString(currentLocal)

    }

    operator fun invoke(
        text: Guard?
    ): String {
        val currentLocal = CurrentLocal.local
        if (text == null) return ""
        return when (currentLocal) {
            0 -> text.name
            1 -> text.nameRu
            else -> ""
        }

    }

    operator fun invoke(
        text: Dwelling
    ): String {
        val currentLocal = CurrentLocal.local
        return when (currentLocal) {
            0 -> text.dwellingName
            1 -> text.dwellingNameRu
            else -> ""
        }

    }

    operator fun invoke(
        text: AdditionalValueItem
    ): String {
        val currentLocal = CurrentLocal.local
        return when (currentLocal) {
            0 -> text.name
            1 -> text.nameRu
            else -> ""
        }

    }

    operator fun invoke(
        text: SearchItem
    ): String {
        val currentLocal = CurrentLocal.local
        return when (currentLocal) {
            0 -> text.itemName
            1 -> text.itemNameRu
            else -> ""
        }

    }

    private fun TextWithLocalization.takeString(locale: Int): String {
        return when (locale) {
            0 -> this.enText
            1 -> this.ruText
            else -> ""
        }
    }

}