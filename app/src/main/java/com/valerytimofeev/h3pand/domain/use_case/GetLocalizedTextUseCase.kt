package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.data.local.AdditionalValueItem
import com.valerytimofeev.h3pand.data.local.Dwelling
import com.valerytimofeev.h3pand.data.local.Guard
import com.valerytimofeev.h3pand.domain.model.CurrentLocal
import com.valerytimofeev.h3pand.data.additional_data.TextWithLocalization

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

    private fun TextWithLocalization.takeString(locale: Int): String {
        return when (locale) {
            0 -> this.enText
            1 -> this.ruText
            else -> ""
        }
    }

}