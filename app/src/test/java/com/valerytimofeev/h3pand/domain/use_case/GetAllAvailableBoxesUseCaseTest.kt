package com.valerytimofeev.h3pand.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.valerytimofeev.h3pand.data.local.additional_data.*
import com.valerytimofeev.h3pand.data.local.database.BoxValueItem
import com.valerytimofeev.h3pand.domain.use_case.calculation_pand_use_case.RemoveUnnecessaryBoxesUseCase
import com.valerytimofeev.h3pand.repositories.local.FakePandRepository
import com.valerytimofeev.h3pand.utils.Resource
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetAllAvailableBoxesUseCaseTest() {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getAllAvailableBoxes: GetAllAvailableBoxesUseCase
    private lateinit var removeUnnecessaryBoxes: RemoveUnnecessaryBoxesUseCase


    @Before
    fun setup() {
        removeUnnecessaryBoxes = RemoveUnnecessaryBoxesUseCase()
        getAllAvailableBoxes =
            GetAllAvailableBoxesUseCase(FakePandRepository(), removeUnnecessaryBoxes)
        Dispatchers.setMain(mainThreadSurrogate)
        mockkObject(MapSettings.JC)
        every { MapSettings.JC.boxRestrictions } returns BoxRestrictions(
            null,
            null
        )
        every { MapSettings.JC.valueRanges } returns listOf(
            ZoneSettings(
                TextWithLocalization("Start", "Стартовая"),
                3,
                listOf(
                    ValueRange(0, 500..5000, 10),
                    ValueRange(1, 6000..10000, 5),
                    ValueRange(2, 12000..25000, 1),
                ),
            ),
            ZoneSettings(
                TextWithLocalization("Central", "Центр"),
                5,
                listOf(
                    ValueRange(0, 25000..30000, 9),
                    ValueRange(1, 31000..35000, 6),
                    ValueRange(2, 36000..40000, 3),
                )
            ),
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `Get all boxes, valid input, return success`() = runTest {

        val result = getAllAvailableBoxes(
            mapSettings = MapSettings.JC,
            currentZone = 0,
            castle = 1,
            castleZones = 1,
            zones = 5
        )
        assertThat(result.data!!.size).isEqualTo(8)
        assertThat(result.data!![1]).isEqualTo(
            BoxValueItem(
                id = 2,
                boxContent = "item 2",
                boxContentRu = "предмет 2",
                value = 7500,
                img = "img",
                type = "Exp"
            )
        )
    }

    @Test
    fun `Get all boxes, different castle, return success`() = runTest {

        val result = getAllAvailableBoxes(
            mapSettings = MapSettings.JC,
            currentZone = 0,
            castle = 9,
            castleZones = 1,
            zones = 5
        )
        assertThat(result.data!!.size).isEqualTo(9)
    }

    @Test
    fun `Get all boxes, wrong castle, returns error`() = runTest {

        val result = getAllAvailableBoxes(
            mapSettings = MapSettings.JC,
            currentZone = 0,
            castle = 20,
            castleZones = 1,
            zones = 5
        )
        assertThat(result).isEqualTo(Resource.error("An unknown error occurred", null))
    }

    @Test
    fun `Get all boxes, wrong zones, returns error`() = runTest {

        val result = getAllAvailableBoxes(
            mapSettings = MapSettings.JC,
            currentZone = 0,
            castle = 20,
            castleZones = 10,
            zones = 5
        )
        assertThat(result).isEqualTo(Resource.error("An unknown error occurred", null))
    }

    @Test
    fun `Get all boxes, map restricted types , return success`() = runTest {
        every { MapSettings.JC.boxRestrictions } returns BoxRestrictions(
            restrictedTypes = listOf("Exp"),
            null
        )
        every { MapSettings.JC.valueRanges } returns listOf(
            ZoneSettings(
                TextWithLocalization("Start", "Стартовая"),
                3,
                listOf(
                    ValueRange(0, 500..5000, 10),
                    ValueRange(1, 6000..10000, 5),
                    ValueRange(2, 12000..25000, 1),
                ),
            ),
            ZoneSettings(
                TextWithLocalization("Central", "Центр"),
                5,
                listOf(
                    ValueRange(0, 25000..30000, 9),
                    ValueRange(1, 31000..35000, 6),
                    ValueRange(2, 36000..40000, 3),
                )
            ),
        )
        val result = getAllAvailableBoxes(
            mapSettings = MapSettings.JC,
            currentZone = 0,
            castle = 1,
            castleZones = 1,
            zones = 5
        )
        assertThat(result.data!!.size).isEqualTo(6)
        assertThat(result.data!!.map { it.type }.distinct()).isEqualTo(
            listOf(
                "Gold",
                "Spell",
                "Unit"
            )
        )
    }

    @Test
    fun `Get all boxes, map restricted Ids , return success`() = runTest {
        every { MapSettings.JC.boxRestrictions } returns BoxRestrictions(
            null,
            listOf(3, 5, 7)
        )
        every { MapSettings.JC.valueRanges } returns listOf(
            ZoneSettings(
                TextWithLocalization("Start", "Стартовая"),
                3,
                listOf(
                    ValueRange(0, 500..5000, 10),
                    ValueRange(1, 6000..10000, 5),
                    ValueRange(2, 12000..25000, 1),
                ),
            ),
            ZoneSettings(
                TextWithLocalization("Central", "Центр"),
                5,
                listOf(
                    ValueRange(0, 25000..30000, 9),
                    ValueRange(1, 31000..35000, 6),
                    ValueRange(2, 36000..40000, 3),
                )
            ),
        )
        val result = getAllAvailableBoxes(
            mapSettings = MapSettings.JC,
            currentZone = 0,
            castle = 1,
            castleZones = 1,
            zones = 5
        )
        assertThat(result.data!!.size).isEqualTo(5)
        assertThat(result.data!!.map { it.id }).isEqualTo(listOf(1, 2, 4, 6, 1001))
    }

    @Test
    fun `Get all boxes, map restricted, same item removed several times, return success`() = runTest {
        every { MapSettings.JC.boxRestrictions } returns BoxRestrictions(
            listOf("Gold", "Exp", "Exp"),
            listOf(1, 2, 3, 3)
        )
        every { MapSettings.JC.valueRanges } returns listOf(
            ZoneSettings(
                TextWithLocalization("Start", "Стартовая"),
                3,
                listOf(
                    ValueRange(0, 500..5000, 10),
                    ValueRange(1, 6000..10000, 5),
                    ValueRange(2, 12000..25000, 1),
                ),
            ),
            ZoneSettings(
                TextWithLocalization("Central", "Центр"),
                5,
                listOf(
                    ValueRange(0, 25000..30000, 9),
                    ValueRange(1, 31000..35000, 6),
                    ValueRange(2, 36000..40000, 3),
                )
            ),
        )
        val result = getAllAvailableBoxes(
            mapSettings = MapSettings.JC,
            currentZone = 0,
            castle = 1,
            castleZones = 1,
            zones = 5
        )
        assertThat(result.data!!.size).isEqualTo(2)
        assertThat(result.data!!.map { it.id }).isEqualTo(listOf(6, 1001))
    }

    @Test
    fun `Get all boxes, zone restricted both type and id, return success`() = runTest {
        every { MapSettings.JC.boxRestrictions } returns BoxRestrictions(
            null,
            null
        )
        every { MapSettings.JC.valueRanges } returns listOf(
            ZoneSettings(
                TextWithLocalization("Start", "Стартовая"),
                3,
                listOf(
                    ValueRange(0, 500..5000, 10),
                    ValueRange(1, 6000..10000, 5),
                    ValueRange(2, 12000..25000, 1),
                ),
                BoxRestrictions(
                    restrictedTypes = listOf("Exp", "Spell"),
                    restrictedIds = listOf(1, 4, 7)
                )
            ),

            ZoneSettings(
                TextWithLocalization("Central", "Центр"),
                5,
                listOf(
                    ValueRange(0, 25000..30000, 9),
                    ValueRange(1, 31000..35000, 6),
                    ValueRange(2, 36000..40000, 3),
                )
            ),
        )
        val result = getAllAvailableBoxes(
            mapSettings = MapSettings.JC,
            currentZone = 0,
            castle = 1,
            castleZones = 1,
            zones = 5
        )
        assertThat(result.data!!.size).isEqualTo(1)
    }

    @Test
    fun `Get all boxes, zone restricted all types, return error`() = runTest {
        every { MapSettings.JC.boxRestrictions } returns BoxRestrictions(
            null,
            null
        )
        every { MapSettings.JC.valueRanges } returns listOf(
            ZoneSettings(
                TextWithLocalization("Start", "Стартовая"),
                3,
                listOf(
                    ValueRange(0, 500..5000, 10),
                    ValueRange(1, 6000..10000, 5),
                    ValueRange(2, 12000..25000, 1),
                ),
                BoxRestrictions(
                    restrictedTypes = listOf("Exp", "Spell", "Gold", "Unit"),
                    null
                )
            ),

            ZoneSettings(
                TextWithLocalization("Central", "Центр"),
                5,
                listOf(
                    ValueRange(0, 25000..30000, 9),
                    ValueRange(1, 31000..35000, 6),
                    ValueRange(2, 36000..40000, 3),
                )
            ),
        )
        val result = getAllAvailableBoxes(
            mapSettings = MapSettings.JC,
            currentZone = 0,
            castle = 1,
            castleZones = 1,
            zones = 5
        )
        assertThat(result).isEqualTo(Resource.error("Error: Wrong map settings", null))
    }

}