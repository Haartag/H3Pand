package com.valerytimofeev.h3pand.domain.use_case

import com.google.common.truth.Truth.assertThat
import com.valerytimofeev.h3pand.data.additional_data.TextWithLocalization
import com.valerytimofeev.h3pand.data.local.Guard
import com.valerytimofeev.h3pand.repositories.local.FakePandRepository
import com.valerytimofeev.h3pand.domain.model.BoxWithDropPercent
import com.valerytimofeev.h3pand.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetBoxesUseCaseTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private lateinit var getBox: GetBoxesUseCase
    private lateinit var getBoxForGuardRange: GetBoxForGuardRangeUseCase
    private lateinit var getGuardCharacteristicsUseCase: GetGuardCharacteristicsUseCase
    private lateinit var getBoxWithPercentUseCase: GetBoxWithPercentUseCase
    private lateinit var getUnitDropCoefficientUseCase: GetUnitDropCoefficientUseCase
    private lateinit var getZoneValueRangeUseCase: GetZoneValueRangeUseCase

    @Before
    fun setup() {
        getBoxForGuardRange = GetBoxForGuardRangeUseCase(FakePandRepository())
        getGuardCharacteristicsUseCase = GetGuardCharacteristicsUseCase()
        getUnitDropCoefficientUseCase = GetUnitDropCoefficientUseCase(FakePandRepository())
        getZoneValueRangeUseCase = GetZoneValueRangeUseCase()
        getBoxWithPercentUseCase = GetBoxWithPercentUseCase(getUnitDropCoefficientUseCase)
        getBox = GetBoxesUseCase(
            getGuardCharacteristicsUseCase,
            getBoxForGuardRange,
            getBoxWithPercentUseCase,
            getZoneValueRangeUseCase
        )

        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `Get Boxes, valid input, returns success`() = runTest {

        val guardUnit = Guard("testUnit", "тестЮнит", 190, 20, 30, "img")
        val result = getBox(
            guardUnit = guardUnit,
            castle = 1,
            castleZones = 1,
            guardRangeIndex = 4,
            zoneType = 0,
            additionalValue = 0,
            week = 1,
            mapName = "JC"
        )

        assertThat(result).isEqualTo(
            Resource.success(
                listOf<BoxWithDropPercent>(
                    BoxWithDropPercent(
                        name = TextWithLocalization("item 2", "предмет 2"),
                        dropChance = 85.2,
                        mostLikelyGuard = 34,
                        range = 26..42,
                        type = "Exp",
                        img = "img"
                    ),
                    BoxWithDropPercent(
                        name = TextWithLocalization("test name 1 60", "тестовое имя 1 60"),
                        dropChance = 14.5,
                        mostLikelyGuard = 25,
                        range = 20..31,
                        type = "Unit",
                        img = "img1"
                    ),
                    BoxWithDropPercent(
                        name = TextWithLocalization("item 3", "предмет 3"),
                        dropChance = 0.2,
                        mostLikelyGuard = 49,
                        range = 46..49,
                        type = "Spell",
                        img = "img"
                    )
                )
            )
        )
    }

    @Test
    fun `Get Boxes, valid input, 1-4 guard range, returns success`() = runTest {

        val guardUnit = Guard("testUnit", "тестЮнит", 6000, 3, 8, "img")
        val result = getBox(
            guardUnit = guardUnit,
            castle = 5,
            castleZones = 1,
            guardRangeIndex = 1,
            zoneType = 0,
            additionalValue = 0,
            week = 1,
            mapName = "JC"
        )

        assertThat(result).isEqualTo(
            Resource.success(
                listOf<BoxWithDropPercent>(
                    BoxWithDropPercent(
                        name = TextWithLocalization("item 7", "предмет 7"),
                        dropChance = 100.0,
                        mostLikelyGuard = 4,
                        range = 4..4,
                        type = "Gold",
                        img = "img"
                    )
                )
            )
        )
    }

    @Test
    fun `Get Boxes, valid input, week correction, returns success`() = runTest {

        val guardUnit = Guard("testUnit", "тестЮнит", 190, 20, 30, "img")
        val result = getBox(
            guardUnit = guardUnit,
            castle = 1,
            castleZones = 1,
            guardRangeIndex = 4,
            zoneType = 0,
            additionalValue = 0,
            week = 3,
            mapName = "JC"
        )

        assertThat(result).isEqualTo(
            Resource.success(
                listOf<BoxWithDropPercent>(
                    BoxWithDropPercent(
                        name = TextWithLocalization("item 2", "предмет 2"),
                        dropChance = 84.9,
                        mostLikelyGuard = 42,
                        range = 32..49,
                        type = "Exp",
                        img = "img"
                    ),
                    BoxWithDropPercent(
                        name = TextWithLocalization("test name 1 60", "тестовое имя 1 60"),
                        dropChance = 15.1,
                        mostLikelyGuard = 31,
                        range = 25..38,
                        type = "Unit",
                        img = "img1"
                    )
                )
            )
        )
    }

    @Test
    fun `Get Boxes, valid input, too big week correction, returns error`() = runTest {

        val guardUnit = Guard("testUnit", "тестЮнит", 201, 20, 30, "img")
        val result = getBox(
            guardUnit = guardUnit,
            castle = 1,
            castleZones = 1,
            guardRangeIndex = 4,
            zoneType = 0,
            additionalValue = 0,
            week = 15,
            mapName = "JC"
        )

        assertThat(result).isEqualTo(Resource.error(msg = "Too weak unit", data = null))
    }

    @Test
    fun `Get Boxes, additional value, returns success`() = runTest {

        val guardUnit = Guard("testUnit", "тестЮнит", 190, 20, 30, "img")
        val result = getBox(
            guardUnit = guardUnit,
            castle = 1,
            castleZones = 1,
            guardRangeIndex = 4,
            zoneType = 0,
            additionalValue = 5000,
            week = 1,
            mapName = "JC"
        )

        assertThat(result).isEqualTo(
            Resource.success(
                listOf<BoxWithDropPercent>(
                    BoxWithDropPercent(
                        name = TextWithLocalization("item 1", "предмет 1"),
                        dropChance = 100.0,
                        mostLikelyGuard = 49,
                        range = 46..49,
                        type = "Gold",
                        img = "img"
                    ),
                )
            )
        )
    }

    @Test
    fun `Get Boxes, too big additional value, returns error`() = runTest {

        val guardUnit = Guard("testUnit", "тестЮнит", 190, 20, 30, "img")
        val result = getBox(
            guardUnit = guardUnit,
            castle = 1,
            castleZones = 1,
            guardRangeIndex = 4,
            zoneType = 0,
            additionalValue = 22000,
            week = 1,
            mapName = "JC"
        )
        assertThat(result).isEqualTo(Resource.error(msg = "No boxes found", data = null))
    }

    @Test
    fun `Get Boxes, additional value and week correction, returns success`() = runTest {

        val guardUnit = Guard("testUnit", "тестЮнит", 190, 20, 30, "img")
        val result = getBox(
            guardUnit = guardUnit,
            castle = 1,
            castleZones = 1,
            guardRangeIndex = 4,
            zoneType = 0,
            additionalValue = 4000,
            week = 3,
            mapName = "JC"
        )
        assertThat(result).isEqualTo(
            Resource.success(
                listOf<BoxWithDropPercent>(
                    BoxWithDropPercent(
                        name = TextWithLocalization("item 1", "предмет 1"),
                        dropChance = 100.0,
                        mostLikelyGuard = 49,
                        range = 46..49,
                        type = "Gold",
                        img = "img"
                    ),
                )
            )
        )
    }

    @Test
    fun `Get Boxes, too big additional value and week correction, returns error`() = runTest {

        val guardUnit = Guard("testUnit", "тестЮнит", 201, 20, 30, "img")
        val result = getBox(
            guardUnit = guardUnit,
            castle = 1,
            castleZones = 1,
            guardRangeIndex = 4,
            zoneType = 0,
            additionalValue = 20000,
            week = 15,
            mapName = "JC"
        )
        assertThat(result).isEqualTo(Resource.error(msg = "Too weak unit", data = null))
    }

    @Test
    fun `Get Boxes, different castle, returns success`() = runTest {

        val guardUnit = Guard("testUnit", "тестЮнит", 190, 20, 30, "img")
        val result = getBox(
            guardUnit = guardUnit,
            castle = 3,
            castleZones = 1,
            guardRangeIndex = 4,
            zoneType = 0,
            additionalValue = 0,
            week = 1,
            mapName = "JC"
        )

        assertThat(result).isEqualTo(
            Resource.success(
                listOf<BoxWithDropPercent>(
                    BoxWithDropPercent(
                        name = TextWithLocalization("item 2", "предмет 2"),
                        dropChance = 90.9,
                        mostLikelyGuard = 34,
                        range = 26..42,
                        type = "Exp",
                        img = "img"
                    ),
                    BoxWithDropPercent(
                        name = TextWithLocalization("test name 4 45", "тестовое имя 4 45"),
                        dropChance = 8.8,
                        mostLikelyGuard = 46,
                        range = 35..49,
                        type = "Unit",
                        img = "img1"
                    ),
                    BoxWithDropPercent(
                        name = TextWithLocalization("item 3", "предмет 3"),
                        dropChance = 0.3,
                        mostLikelyGuard = 49,
                        range = 46..49,
                        type = "Spell",
                        img = "img"
                    )
                )
            )
        )
    }

    @Test
    fun `Get Boxes, different castle zones, returns success`() = runTest {

        val guardUnit = Guard("testUnit", "тестЮнит", 190, 20, 30, "img")
        val result = getBox(
            guardUnit = guardUnit,
            castle = 1,
            castleZones = 3,
            guardRangeIndex = 4,
            zoneType = 0,
            additionalValue = 0,
            week = 1,
            mapName = "JC"
        )

        assertThat(result).isEqualTo(
            Resource.success(
                listOf<BoxWithDropPercent>(
                    BoxWithDropPercent(
                        name = TextWithLocalization("item 2", "предмет 2"),
                        dropChance = 87.4,
                        mostLikelyGuard = 34,
                        range = 26..42,
                        type = "Exp",
                        img = "img"
                    ),
                    BoxWithDropPercent(
                        name = TextWithLocalization("test name 1 60", "тестовое имя 1 60"),
                        dropChance = 12.4,
                        mostLikelyGuard = 36,
                        range = 27..45,
                        type = "Unit",
                        img = "img1"
                    ),
                    BoxWithDropPercent(
                        name = TextWithLocalization("item 3", "предмет 3"),
                        dropChance = 0.2,
                        mostLikelyGuard = 49,
                        range = 46..49,
                        type = "Spell",
                        img = "img"
                    )
                )
            )
        )
    }

    @Test
    fun `Get Boxes, wrong castle zones, returns error`() = runTest {

        val guardUnit = Guard("testUnit", "тестЮнит", 190, 20, 30, "img")
        val result = getBox(
            guardUnit = guardUnit,
            castle = 1,
            castleZones = 10,
            guardRangeIndex = 4,
            zoneType = 0,
            additionalValue = 0,
            week = 1,
            mapName = "JC"
        )

        assertThat(result).isEqualTo(Resource.error(msg = "Error: Wrong map settings", data = null))
    }

    @Test
    fun `Get Boxes, valid input, different zone type, returns success`() = runTest {

        val guardUnit = Guard("testUnit", "тестЮнит", 6000, 3, 8, "img")
        val result = getBox(
            guardUnit = guardUnit,
            castle = 5,
            castleZones = 1,
            guardRangeIndex = 1,
            zoneType = 1,
            additionalValue = 0,
            week = 1,
            mapName = "JC"
        )

        assertThat(result).isEqualTo(
            Resource.success(
                listOf<BoxWithDropPercent>(
                    BoxWithDropPercent(
                        name = TextWithLocalization("item 4", "предмет 4"),
                        dropChance = 100.0,
                        mostLikelyGuard = 4,
                        range = 4..4,
                        type = "Gold",
                        img = "img"
                    )
                )
            )
        )
    }




}