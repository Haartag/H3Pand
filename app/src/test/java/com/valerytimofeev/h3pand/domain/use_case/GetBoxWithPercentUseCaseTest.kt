package com.valerytimofeev.h3pand.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.valerytimofeev.h3pand.data.additional_data.MapSettings
import com.valerytimofeev.h3pand.data.additional_data.TextWithLocalization
import com.valerytimofeev.h3pand.data.additional_data.ValueRange
import com.valerytimofeev.h3pand.data.additional_data.ZoneSettings
import com.valerytimofeev.h3pand.data.local.BoxValueItem
import com.valerytimofeev.h3pand.domain.model.BoxWithDropPercent
import com.valerytimofeev.h3pand.domain.model.Difficult
import com.valerytimofeev.h3pand.domain.model.GuardCharacteristics
import com.valerytimofeev.h3pand.repositories.local.FakePandRepository
import com.valerytimofeev.h3pand.utils.*
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
class GetBoxWithPercentUseCaseTest() {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getUnitDropCoefficientUseCase: GetUnitDropCoefficientUseCase
    private lateinit var getBoxWithPercentUseCase: GetBoxWithPercentUseCase

    @Before
    fun setup() {
        getUnitDropCoefficientUseCase = GetUnitDropCoefficientUseCase(FakePandRepository())
        getBoxWithPercentUseCase = GetBoxWithPercentUseCase(getUnitDropCoefficientUseCase)
        Dispatchers.setMain(mainThreadSurrogate)
        mockkObject(MapSettings.JC)
        every { MapSettings.JC.valueRanges } returns listOf(
            ZoneSettings(
                TextWithLocalization("Start", "Стартовая"),
                3,
                listOf(
                    ValueRange(0, 500..5000, 10),
                    ValueRange(1, 6000..10000, 5),
                    ValueRange(2, 12000..25000, 1),
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
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `Get BoxWithDropPercent, valid input, all guards in range`() = runTest {
        val boxValueItem = BoxValueItem(
            1,
            "5000exp",
            "5000 опыт",
            6000,
            "exp",
            "Exp"
        )
        val guardValue = GuardCharacteristics(16, 13, 65, 86)
        val difficult = Difficult.THREE
        val unitValue = 190
        val week = 1
        val additionalValue = 0
        val chosenGuardRange = 20..49
        val castle = 1
        val numberOfZones = 5.0f
        val numberOfUnitZones = 1.0f
        val mapSettings = MapSettings.JC
        val zoneType = 0

        val value = getBoxWithPercentUseCase(
            boxValueItem,
            guardValue,
            difficult,
            unitValue,
            week,
            chosenGuardRange,
            additionalValue,
            castle,
            numberOfZones,
            numberOfUnitZones,
            mapSettings,
            zoneType
        )
        assertThat(value.data).isEqualTo(
            BoxWithDropPercent(
                name =  TextWithLocalization("5000exp", "5000 опыт"),
                dropChance = 11300.0,
                mostLikelyGuard = 26,
                range = 20..32,
                type = "Exp",
                img = "exp"
            )
        )
    }

    @Test
    fun `Get BoxWithDropPercent, different town, all guards in range`() = runTest {
        val boxValueItem = BoxValueItem(
            1,
            "5000exp",
            "5000 опыт",
            6000,
            "exp",
            "Exp"
        )
        val guardValue = GuardCharacteristics(16, 13, 65, 86)
        val difficult = Difficult.THREE
        val unitValue = 190
        val week = 1
        val additionalValue = 0
        val chosenGuardRange = 20..49
        val castle = 3
        val numberOfZones = 5.0f
        val numberOfUnitZones = 1.0f
        val mapSettings = MapSettings.JC
        val zoneType = 0

        val value = getBoxWithPercentUseCase(
            boxValueItem,
            guardValue,
            difficult,
            unitValue,
            week,
            chosenGuardRange,
            additionalValue,
            castle,
            numberOfZones,
            numberOfUnitZones,
            mapSettings,
            zoneType
        )
        assertThat(value.data).isEqualTo(
            BoxWithDropPercent(
                name =  TextWithLocalization("5000exp", "5000 опыт"),
                dropChance = 10800.0,
                mostLikelyGuard = 26,
                range = 20..32,
                type = "Exp",
                img = "exp"
            )
        )
    }

    @Test
    fun `Get BoxWithDropPercent, wrong zones, all guards in range`() = runTest {
        val boxValueItem = BoxValueItem(
            1,
            "5000exp",
            "5000 опыт",
            6000,
            "exp",
            "Exp"
        )
        val guardValue = GuardCharacteristics(16, 13, 65, 86)
        val difficult = Difficult.THREE
        val unitValue = 190
        val week = 1
        val additionalValue = 0
        val chosenGuardRange = 20..49
        val castle = 1
        val numberOfZones = 5.0f
        val numberOfUnitZones = 10.0f
        val mapSettings = MapSettings.JC
        val zoneType = 0

        val value = getBoxWithPercentUseCase(
            boxValueItem,
            guardValue,
            difficult,
            unitValue,
            week,
            chosenGuardRange,
            additionalValue,
            castle,
            numberOfZones,
            numberOfUnitZones,
            mapSettings,
            zoneType
        )
        assertThat(value).isEqualTo(Resource.error("An unknown error occurred", null))
    }

    @Test
    fun `Get BoxWithDropPercent, valid input, some guards not in range`() = runTest {
        val boxValueItem = BoxValueItem(
            1,
            "5000exp",
            "5000 опыт",
            6000,
            "exp",
            "Exp"
        )
        val guardValue = GuardCharacteristics(30, 24, 65, 86)
        val difficult = Difficult.THREE
        val unitValue = 201
        val week = 1
        val additionalValue = 0
        val chosenGuardRange = 20..49
        val castle = 1
        val numberOfZones = 5.0f
        val numberOfUnitZones = 1.0f
        val mapSettings = MapSettings.JC
        val zoneType = 0

        val value = getBoxWithPercentUseCase(
            boxValueItem,
            guardValue,
            difficult,
            unitValue,
            week,
            chosenGuardRange,
            additionalValue,
            castle,
            numberOfZones,
            numberOfUnitZones,
            mapSettings,
            zoneType
        )

        assertThat(value.data!!.dropChance).isAtLeast(7840.0)
        assertThat(value.data!!.dropChance).isAtMost(7841.0)
    }

    @Test
    fun `Get BoxWithDropPercent, too high value, returns error`() = runTest {
        val boxValueItem = BoxValueItem(1, "1LvlSpells", "Заклинания 1 уровня", 5000, "1LvlSpells", "Spell")
        val guardValue = GuardCharacteristics(16, 13, 65, 86)
        val difficult = Difficult.THREE
        val unitValue = 1350
        val week = 1
        val additionalValue = 0
        val chosenGuardRange = 20..49
        val castle = 1
        val numberOfZones = 5.0f
        val numberOfUnitZones = 1.0f
        val mapSettings = MapSettings.JC
        val zoneType = 0

        val value = getBoxWithPercentUseCase(
            boxValueItem,
            guardValue,
            difficult,
            unitValue,
            week,
            chosenGuardRange,
            additionalValue,
            castle,
            numberOfZones,
            numberOfUnitZones,
            mapSettings,
            zoneType
        )

        assertThat(value).isEqualTo(Resource.error("An unknown error occurred", null))
    }

    @Test
    fun `Get BoxWithDropPercent, valid input, week after 1`() = runTest {
        val boxValueItem = BoxValueItem(1, "5000exp", "5000 опыт", 6000, "exp", "Exp")
        val guardValue = GuardCharacteristics(16, 13, 65, 86)
        val difficult = Difficult.THREE
        val unitValue = 190
        val week = 3
        val additionalValue = 0
        val chosenGuardRange = 20..49
        val castle = 1
        val numberOfZones = 5.0f
        val numberOfUnitZones = 1.0f
        val mapSettings = MapSettings.JC
        val zoneType = 0

        val value = getBoxWithPercentUseCase(
            boxValueItem,
            guardValue,
            difficult,
            unitValue,
            week,
            chosenGuardRange,
            additionalValue,
            castle,
            numberOfZones,
            numberOfUnitZones,
            mapSettings,
            zoneType
        )
        assertThat(value.data).isEqualTo(
            BoxWithDropPercent(
                name =  TextWithLocalization("5000exp", "5000 опыт"),
                dropChance = 11300.0,
                mostLikelyGuard = 32,
                range = 25..39,
                type = "Exp",
                img = "exp"
            )
        )
    }

    @Test
    fun `!! Get BoxWithDropPercent, valid input, part of week-modified range above guard range`() = runTest {
        val boxValueItem = BoxValueItem(1, "5000exp", "5000 опыт", 6000, "exp", "Exp")
        val guardValue = GuardCharacteristics(16, 13, 65, 86)
        val difficult = Difficult.THREE
        val unitValue = 190
        val week = 7
        val additionalValue = 0
        val chosenGuardRange = 20..49
        val castle = 1
        val numberOfZones = 5.0f
        val numberOfUnitZones = 1.0f
        val mapSettings = MapSettings.JC
        val zoneType = 0

        val value = getBoxWithPercentUseCase(
            boxValueItem,
            guardValue,
            difficult,
            unitValue,
            week,
            chosenGuardRange,
            additionalValue,
            castle,
            numberOfZones,
            numberOfUnitZones,
            mapSettings,
            zoneType
        )

        assertThat(value.data!!.range).isEqualTo((36..49))
        assertThat(value.data!!.dropChance).isAtLeast(7840.0)
        assertThat(value.data!!.dropChance).isAtMost(7841.0)
    }

    @Test
    fun `!! Get BoxWithDropPercent, valid input, all week-modified range above guard range`() = runTest {
        val boxValueItem = BoxValueItem(1, "5000exp", "5000 опыт", 6000, "exp", "Exp")
        val guardValue = GuardCharacteristics(16, 13, 65, 86)
        val difficult = Difficult.THREE
        val unitValue = 190
        val week = 11
        val additionalValue = 0
        val chosenGuardRange = 20..49
        val castle = 1
        val numberOfZones = 5.0f
        val numberOfUnitZones = 1.0f
        val mapSettings = MapSettings.JC
        val zoneType = 0

        val value = getBoxWithPercentUseCase(
            boxValueItem,
            guardValue,
            difficult,
            unitValue,
            week,
            chosenGuardRange,
            additionalValue,
            castle,
            numberOfZones,
            numberOfUnitZones,
            mapSettings,
            zoneType
        )
        assertThat(value).isEqualTo(Resource.error("An unknown error occurred", null))
    }


}
