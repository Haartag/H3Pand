package com.valerytimofeev.h3pand.domain.use_case.dialog_use_case

import com.google.common.truth.Truth
import com.valerytimofeev.h3pand.data.local.AdditionalValueItem
import com.valerytimofeev.h3pand.data.local.Dwelling
import com.valerytimofeev.h3pand.domain.model.SearchItem
import org.junit.Before
import org.junit.Test


class ConvertToSearchListUseCaseTest() {

    private lateinit var convertToSearchListUseCase: ConvertToSearchListUseCase

    @Before
    fun setup() {
        convertToSearchListUseCase = ConvertToSearchListUseCase()
    }

    @Test
    fun `Merge addValue List and dwelling List to searchItem List, both input lists`() {
        val addValueList = listOf(
            AdditionalValueItem(
                1,
                "add name 1",
                "доп. имя 1",
                1400,
                "Misc.",
                "Разное",
                "Morale/Luck",
                "Мораль/Удача",
                0
            ),
            AdditionalValueItem(
                2,
                "add name 2",
                "доп. имя 2",
                2000,
                "Artifact",
                "Артефакты",
                "Treasure artifact",
                "Артефакты-сокровища",
                0
            )
        )
        val dwellingList = listOf(
            Dwelling(
                dwellingName = "test dwelling 2",
                dwellingNameRu = "тестовое жилище 2",
                name = "test name 2",
                nameRu = "тестовое имя 2",
                AIValue = 250,
                weeklyGain = 7,
                castle = 3
            )
        )

        val value = convertToSearchListUseCase(
            addValueList, dwellingList
        )

        Truth.assertThat(value).isEqualTo(
            listOf(
                SearchItem(
                    itemName = "add name 1",
                    itemNameRu = "доп. имя 1",
                    isDwelling = false,
                    addItemValue = 1400,
                    unitValue = null,
                    weeklyGain = null,
                    castle = null
                ),
                SearchItem(
                    itemName = "add name 2",
                    itemNameRu = "доп. имя 2",
                    isDwelling = false,
                    addItemValue = 2000,
                    unitValue = null,
                    weeklyGain = null,
                    castle = null
                ),
                SearchItem(
                    itemName = "test dwelling 2",
                    itemNameRu = "тестовое жилище 2",
                    isDwelling = true,
                    addItemValue = null,
                    unitValue = 250,
                    weeklyGain = 7,
                    castle = 3
                )
            )
        )
    }

    @Test
    fun `Convert addValue List to searchItem List, empty dwelling list`() {
        val addValueList = listOf(
            AdditionalValueItem(
                1,
                "add name 1",
                "доп. имя 1",
                1400,
                "Misc.",
                "Разное",
                "Morale/Luck",
                "Мораль/Удача",
                0
            ),
            AdditionalValueItem(
                2,
                "add name 2",
                "доп. имя 2",
                2000,
                "Artifact",
                "Артефакты",
                "Treasure artifact",
                "Артефакты-сокровища",
                0
            )
        )
        val dwellingList = emptyList<Dwelling>()

        val value = convertToSearchListUseCase(
            addValueList, dwellingList
        )

        Truth.assertThat(value).isEqualTo(
            listOf(
                SearchItem(
                    itemName = "add name 1",
                    itemNameRu = "доп. имя 1",
                    isDwelling = false,
                    addItemValue = 1400,
                    unitValue = null,
                    weeklyGain = null,
                    castle = null
                ),
                SearchItem(
                    itemName = "add name 2",
                    itemNameRu = "доп. имя 2",
                    isDwelling = false,
                    addItemValue = 2000,
                    unitValue = null,
                    weeklyGain = null,
                    castle = null
                )
            )
        )
    }

    @Test
    fun `Convert dwelling List to searchItem List, empty addValue list`() {
        val addValueList = emptyList<AdditionalValueItem>()
        val dwellingList = listOf(
            Dwelling(
                dwellingName = "test dwelling 2",
                dwellingNameRu = "тестовое жилище 2",
                name = "test name 2",
                nameRu = "тестовое имя 2",
                AIValue = 250,
                weeklyGain = 7,
                castle = 3
            )
        )

        val value = convertToSearchListUseCase(
            addValueList, dwellingList
        )

        Truth.assertThat(value).isEqualTo(
            listOf(
                SearchItem(
                    itemName = "test dwelling 2",
                    itemNameRu = "тестовое жилище 2",
                    isDwelling = true,
                    addItemValue = null,
                    unitValue = 250,
                    weeklyGain = 7,
                    castle = 3
                )
            )
        )
    }

    @Test
    fun `Merge addValue List and dwelling List to searchItem List, both inputs empty`() {
        val addValueList = emptyList<AdditionalValueItem>()
        val dwellingList = emptyList<Dwelling>()

        val value = convertToSearchListUseCase(
            addValueList, dwellingList
        )

        Truth.assertThat(value).isEqualTo(
            emptyList<SearchItem>()
        )
    }

}