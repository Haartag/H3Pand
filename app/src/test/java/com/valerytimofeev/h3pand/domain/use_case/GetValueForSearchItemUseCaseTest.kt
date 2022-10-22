package com.valerytimofeev.h3pand.domain.use_case

import com.google.common.truth.Truth
import com.valerytimofeev.h3pand.domain.model.SearchItem
import com.valerytimofeev.h3pand.utils.Status
import org.junit.Before
import org.junit.Test


class GetValueForSearchItemUseCaseTest() {

    private lateinit var getValueForSearchItemUseCase: GetValueForSearchItemUseCase
    private lateinit var getDwellingValueUseCase: GetDwellingValueUseCase

    @Before
    fun setup() {
        getDwellingValueUseCase = GetDwellingValueUseCase()
        getValueForSearchItemUseCase = GetValueForSearchItemUseCase(getDwellingValueUseCase)
    }

    @Test
    fun `Get value for search item, addValue search item`() {

        val item = SearchItem(
            itemName = "add name 1",
            itemNameRu = "доп. имя 1",
            isDwelling = false,
            addItemValue = 1400,
            unitValue = null,
            weeklyGain = null,
            castle = null
        )

        val numberOfZones = 5.0f
        val numberOfUnitZones = 1.0f

        val value = getValueForSearchItemUseCase(
            item,
            numberOfZones,
            numberOfUnitZones,
        )
        Truth.assertThat(value.data).isEqualTo(1400)
    }

    @Test
    fun `Get value for search item, addValue search item, unusual number of zones`() {

        val item = SearchItem(
            itemName = "add name 1",
            itemNameRu = "доп. имя 1",
            isDwelling = false,
            addItemValue = 1400,
            unitValue = null,
            weeklyGain = null,
            castle = null
        )

        val numberOfZones = 10.0f
        val numberOfUnitZones = 10.0f

        val value = getValueForSearchItemUseCase(
            item,
            numberOfZones,
            numberOfUnitZones,
        )
        Truth.assertThat(value.data).isEqualTo(1400)
    }

    @Test
    fun `Get value for search item, dwelling search item`() {

        val item = SearchItem(
            itemName = "test dwelling 2",
            itemNameRu = "тестовое жилище 2",
            isDwelling = true,
            addItemValue = null,
            unitValue = 250,
            weeklyGain = 7,
            castle = 3
        )

        val numberOfZones = 5.0f
        val numberOfUnitZones = 1.0f

        val value = getValueForSearchItemUseCase(
            item,
            numberOfZones,
            numberOfUnitZones,
        )
        Truth.assertThat(value.data).isEqualTo(2225)
    }

    @Test
    fun `Get value for search item, dwelling search item, unusual number of zones`() {

        val item = SearchItem(
            itemName = "test dwelling 2",
            itemNameRu = "тестовое жилище 2",
            isDwelling = true,
            addItemValue = null,
            unitValue = 250,
            weeklyGain = 7,
            castle = 3
        )

        val numberOfZones = 10.0f
        val numberOfUnitZones = 10.0f

        val value = getValueForSearchItemUseCase(
            item,
            numberOfZones,
            numberOfUnitZones,
        )
        Truth.assertThat(value.data).isEqualTo(4750)
    }

    @Test
    fun `Get value for search item, empty search item, isDwelling = true`() {

        val item = SearchItem(
            itemName = "test dwelling 2",
            itemNameRu = "тестовое жилище 2",
            isDwelling = true,
            addItemValue = null,
            unitValue = null,
            weeklyGain = null,
            castle = null
        )

        val numberOfZones = 5.0f
        val numberOfUnitZones = 1.0f

        val value = getValueForSearchItemUseCase(
            item,
            numberOfZones,
            numberOfUnitZones,
        )
        Truth.assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `Get value for search item, empty search item, isDwelling = false`() {

        val item = SearchItem(
            itemName = "add name 1",
            itemNameRu = "доп. имя 1",
            isDwelling = false,
            addItemValue = null,
            unitValue = null,
            weeklyGain = null,
            castle = null
        )

        val numberOfZones = 5.0f
        val numberOfUnitZones = 1.0f

        val value = getValueForSearchItemUseCase(
            item,
            numberOfZones,
            numberOfUnitZones,
        )
        Truth.assertThat(value.status).isEqualTo(Status.ERROR)
    }
}