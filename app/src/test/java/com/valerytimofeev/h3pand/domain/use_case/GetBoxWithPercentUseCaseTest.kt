package com.valerytimofeev.h3pand.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.valerytimofeev.h3pand.data.local.additional_data.MapSettings
import com.valerytimofeev.h3pand.data.local.additional_data.TextWithLocalization
import com.valerytimofeev.h3pand.data.local.additional_data.ValueRange
import com.valerytimofeev.h3pand.data.local.additional_data.ZoneSettings
import com.valerytimofeev.h3pand.data.local.database.BoxValueItem
import com.valerytimofeev.h3pand.domain.model.BoxWithGuard
import com.valerytimofeev.h3pand.domain.model.GuardCharacteristics
import com.valerytimofeev.h3pand.domain.model.GuardNumber
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
    fun `Get BoxWithDropPercent, valid input`() = runTest {
        val boxValueItem = BoxValueItem(
            id = 1,
            boxContent = "item 1",
            boxContentRu = "предмет 1",
            value = 5000,
            img = "img",
            type = "Gold"
        )
        val boxWithGuard = BoxWithGuard(
            box = boxValueItem,
            guardNumber = GuardNumber(minGuard = 38, avgGuard = 50, maxGuard = 62)
        )
        val guardValue = GuardCharacteristics(16, 13, 65, 86)
        val week = 1
        val chosenGuardRange = 20..49
        val castle = 1
        val numberOfZones = 5.0f
        val numberOfUnitZones = 1.0f
        val mapSettings = MapSettings.JC
        val zoneType = 0

        val value = getBoxWithPercentUseCase(
            boxWithGuard,
            guardValue,
            week,
            chosenGuardRange,
            castle,
            numberOfZones,
            numberOfUnitZones,
            mapSettings,
            zoneType,
            false
        )
        assertThat(value.data!!.name).isEqualTo(TextWithLocalization("item 1", "предмет 1"))
        assertThat(value.data!!.mostLikelyGuard).isEqualTo(49)
        assertThat(value.data!!.range).isEqualTo(38..49)
        assertThat(value.data!!.type).isEqualTo("Gold")
        assertThat(value.data!!.img).isEqualTo("img")
        assertThat(value.data!!.dropChance).isWithin(0.5).of(15462.0)
    }

    @Test
    fun `Get BoxWithDropPercent, unit box`() = runTest {
        val boxValueItem = BoxValueItem(
            id = 0,
            boxContent = "test name 1 60",
            boxContentRu = "тестовое имя 1 60",
            value = 5760,
            img = "img1",
            type = "Unit"
        )
        val boxWithGuard = BoxWithGuard(
            box = boxValueItem,
            guardNumber = GuardNumber(minGuard = 45, avgGuard = 60, maxGuard = 75)
        )
        val guardValue = GuardCharacteristics(16, 13, 65, 86)
        val week = 1
        val chosenGuardRange = 20..49
        val castle = 1
        val numberOfZones = 5.0f
        val numberOfUnitZones = 1.0f
        val mapSettings = MapSettings.JC
        val zoneType = 0

        val value = getBoxWithPercentUseCase(
            boxWithGuard,
            guardValue,
            week,
            chosenGuardRange,
            castle,
            numberOfZones,
            numberOfUnitZones,
            mapSettings,
            zoneType,
            false
        )
        assertThat(value.data!!.range).isEqualTo(45..49)
        assertThat(value.data!!.dropChance).isWithin(0.5).of(1037.0)
    }


    @Test
    fun `Get BoxWithDropPercent, different town`() = runTest {
        val boxValueItem = BoxValueItem(
            id = 1,
            boxContent = "item 1",
            boxContentRu = "предмет 1",
            value = 5000,
            img = "img",
            type = "Gold"
        )
        val boxWithGuard = BoxWithGuard(
            box = boxValueItem,
            guardNumber = GuardNumber(minGuard = 38, avgGuard = 50, maxGuard = 62)
        )
        val guardValue = GuardCharacteristics(16, 13, 65, 86)
        val week = 1
        val chosenGuardRange = 20..49
        val castle = 10
        val numberOfZones = 5.0f
        val numberOfUnitZones = 1.0f
        val mapSettings = MapSettings.JC
        val zoneType = 0

        val value = getBoxWithPercentUseCase(
            boxWithGuard,
            guardValue,
            week,
            chosenGuardRange,
            castle,
            numberOfZones,
            numberOfUnitZones,
            mapSettings,
            zoneType,
            false
        )
        assertThat(value.data!!.name).isEqualTo(TextWithLocalization("item 1", "предмет 1"))
        assertThat(value.data!!.mostLikelyGuard).isEqualTo(49)
        assertThat(value.data!!.range).isEqualTo(38..49)
    }

    @Test
    fun `Get BoxWithDropPercent, different zones, non-unit box`() = runTest {
        val boxValueItem = BoxValueItem(
            id = 1,
            boxContent = "item 1",
            boxContentRu = "предмет 1",
            value = 5000,
            img = "img",
            type = "Gold"
        )
        val boxWithGuard = BoxWithGuard(
            box = boxValueItem,
            guardNumber = GuardNumber(minGuard = 38, avgGuard = 50, maxGuard = 62)
        )
        val guardValue = GuardCharacteristics(16, 13, 65, 86)
        val week = 1
        val chosenGuardRange = 20..49
        val castle = 1
        val numberOfZones = 5.0f
        val numberOfUnitZones = 2.0f
        val mapSettings = MapSettings.JC
        val zoneType = 0

        val value = getBoxWithPercentUseCase(
            boxWithGuard,
            guardValue,
            week,
            chosenGuardRange,
            castle,
            numberOfZones,
            numberOfUnitZones,
            mapSettings,
            zoneType,
            false
        )
        assertThat(value.data!!.range).isEqualTo(38..49)
        assertThat(value.data!!.dropChance).isWithin(0.5).of(15462.0)
    }

    @Test
    fun `Get BoxWithDropPercent, different zones, unit box`() = runTest {
        val boxValueItem = BoxValueItem(
            id = 0,
            boxContent = "test name 1 60",
            boxContentRu = "тестовое имя 1 60",
            value = 5760,
            img = "img1",
            type = "Unit"
        )
        val boxWithGuard = BoxWithGuard(
            box = boxValueItem,
            guardNumber = GuardNumber(minGuard = 45, avgGuard = 60, maxGuard = 75)
        )
        val guardValue = GuardCharacteristics(16, 13, 65, 86)
        val week = 1
        val chosenGuardRange = 20..49
        val castle = 1
        val numberOfZones = 5.0f
        val numberOfUnitZones = 5.0f
        val mapSettings = MapSettings.JC
        val zoneType = 0

        val value = getBoxWithPercentUseCase(
            boxWithGuard,
            guardValue,
            week,
            chosenGuardRange,
            castle,
            numberOfZones,
            numberOfUnitZones,
            mapSettings,
            zoneType,
            false
        )
        assertThat(value.data!!.range).isEqualTo(45..49)
        assertThat(value.data!!.dropChance).isWithin(0.5).of(990.0)
    }

    @Test
    fun `Get BoxWithDropPercent, too low guard range`() = runTest {
        val boxValueItem = BoxValueItem(
            id = 1,
            boxContent = "item 1",
            boxContentRu = "предмет 1",
            value = 5000,
            img = "img",
            type = "Gold"
        )
        val boxWithGuard = BoxWithGuard(
            box = boxValueItem,
            guardNumber = GuardNumber(minGuard = 38, avgGuard = 50, maxGuard = 62)
        )
        val guardValue = GuardCharacteristics(16, 13, 65, 86)
        val week = 1
        val chosenGuardRange = 10..19
        val castle = 1
        val numberOfZones = 5.0f
        val numberOfUnitZones = 1.0f
        val mapSettings = MapSettings.JC
        val zoneType = 0

        val value = getBoxWithPercentUseCase(
            boxWithGuard,
            guardValue,
            week,
            chosenGuardRange,
            castle,
            numberOfZones,
            numberOfUnitZones,
            mapSettings,
            zoneType,
            false
        )
        assertThat(value).isEqualTo(Resource.error("An unknown error occurred", null))
    }

    @Test
    fun `Get BoxWithDropPercent, too big guard range`() = runTest {
        val boxValueItem = BoxValueItem(
            id = 1,
            boxContent = "item 1",
            boxContentRu = "предмет 1",
            value = 5000,
            img = "img",
            type = "Gold"
        )
        val boxWithGuard = BoxWithGuard(
            box = boxValueItem,
            guardNumber = GuardNumber(minGuard = 38, avgGuard = 50, maxGuard = 62)
        )
        val guardValue = GuardCharacteristics(16, 13, 65, 86)
        val week = 1
        val chosenGuardRange = 100..250
        val castle = 1
        val numberOfZones = 5.0f
        val numberOfUnitZones = 1.0f
        val mapSettings = MapSettings.JC
        val zoneType = 0

        val value = getBoxWithPercentUseCase(
            boxWithGuard,
            guardValue,
            week,
            chosenGuardRange,
            castle,
            numberOfZones,
            numberOfUnitZones,
            mapSettings,
            zoneType,
            false
        )
        assertThat(value).isEqualTo(Resource.error("An unknown error occurred", null))
    }

    @Test
    fun `Get BoxWithDropPercent, week after 1`() = runTest {
        val boxValueItem = BoxValueItem(
            id = 1,
            boxContent = "item 1",
            boxContentRu = "предмет 1",
            value = 5000,
            img = "img",
            type = "Gold"
        )
        val boxWithGuard = BoxWithGuard(
            box = boxValueItem,
            guardNumber = GuardNumber(minGuard = 38, avgGuard = 50, maxGuard = 62)
        )
        val guardValue = GuardCharacteristics(16, 13, 65, 86)
        val week = 2
        val chosenGuardRange = 20..49
        val castle = 1
        val numberOfZones = 5.0f
        val numberOfUnitZones = 1.0f
        val mapSettings = MapSettings.JC
        val zoneType = 0

        val value = getBoxWithPercentUseCase(
            boxWithGuard,
            guardValue,
            week,
            chosenGuardRange,
            castle,
            numberOfZones,
            numberOfUnitZones,
            mapSettings,
            zoneType,
            false
        )
        assertThat(value.data!!.mostLikelyGuard).isEqualTo(49)
        assertThat(value.data!!.range).isEqualTo(41..49)
        assertThat(value.data!!.dropChance).isWithin(0.5).of(7136.0)
    }

    @Test
    fun `!! Get BoxWithDropPercent, valid input`() = runTest {
        val boxValueItem = BoxValueItem(
            id = 1,
            boxContent = "item test",
            boxContentRu = "предмет тест",
            value = 6000,
            img = "exp",
            type = "Exp"
        )
        val boxWithGuard = BoxWithGuard(
            box = boxValueItem,
            guardNumber = GuardNumber(minGuard = 22, avgGuard = 29, maxGuard = 36)
        )
        val guardValue = GuardCharacteristics(16, 13, 65, 86)
        val week = 8
        val chosenGuardRange = 20..49
        val castle = 1
        val numberOfZones = 5.0f
        val numberOfUnitZones = 1.0f
        val mapSettings = MapSettings.JC
        val zoneType = 0



        val value = getBoxWithPercentUseCase(
            boxWithGuard,
            guardValue,
            week,
            chosenGuardRange,
            castle,
            numberOfZones,
            numberOfUnitZones,
            mapSettings,
            zoneType,
            false
        )
        assertThat(value.data!!.mostLikelyGuard).isEqualTo(48)
        assertThat(value.data!!.range).isEqualTo(42..49)
    }
}
