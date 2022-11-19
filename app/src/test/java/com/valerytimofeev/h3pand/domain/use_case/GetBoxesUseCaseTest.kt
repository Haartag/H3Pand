package com.valerytimofeev.h3pand.domain.use_case

import com.google.common.truth.Truth.assertThat
import com.valerytimofeev.h3pand.data.local.additional_data.TextWithLocalization
import com.valerytimofeev.h3pand.data.local.database.Guard
import com.valerytimofeev.h3pand.domain.model.BoxWithDropChance
import com.valerytimofeev.h3pand.repositories.local.FakePandRepository
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
    private lateinit var getGuardCharacteristicsUseCase: GetGuardCharacteristicsUseCase
    private lateinit var getBoxWithPercentUseCase: GetBoxWithPercentUseCase
    private lateinit var getUnitDropCoefficientUseCase: GetUnitDropCoefficientUseCase
    private lateinit var getZoneValueRangeUseCase: GetZoneValueRangeUseCase
    private lateinit var getAllAvailableBoxesUseCase: GetAllAvailableBoxesUseCase
    private lateinit var getValidBoxesAndGuardsUseCase: GetValidBoxesAndGuardsUseCase

    @Before
    fun setup() {
        getGuardCharacteristicsUseCase = GetGuardCharacteristicsUseCase()
        getUnitDropCoefficientUseCase = GetUnitDropCoefficientUseCase(FakePandRepository())
        getZoneValueRangeUseCase = GetZoneValueRangeUseCase()
        getAllAvailableBoxesUseCase = GetAllAvailableBoxesUseCase(FakePandRepository())
        getValidBoxesAndGuardsUseCase = GetValidBoxesAndGuardsUseCase()
        getBoxWithPercentUseCase = GetBoxWithPercentUseCase(getUnitDropCoefficientUseCase)
        getBox = GetBoxesUseCase(
            getGuardCharacteristicsUseCase,
            getBoxWithPercentUseCase,
            getZoneValueRangeUseCase,
            getAllAvailableBoxesUseCase,
            getValidBoxesAndGuardsUseCase
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
            guardRange = 20..49,
            zoneType = 0,
            additionalValue = 0,
            week = 1,
            mapName = "JC"
        )

        assertThat(result).isEqualTo(
            Resource.success(
                listOf(
                    BoxWithDropChance(
                        name = TextWithLocalization("item 2", "предмет 2"),
                        dropChance = 85.0,
                        mostLikelyGuard = 34,
                        range = 26..42,
                        type = "Exp",
                        img = "img"
                    ),
                    BoxWithDropChance(
                        name = TextWithLocalization("test name 1 60", "тестовое имя 1 60"),
                        dropChance = 14.8,
                        mostLikelyGuard = 25,
                        range = 20..31,
                        type = "Unit",
                        img = "img1"
                    ),
                    BoxWithDropChance(
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
            guardRange = 1..4,
            zoneType = 0,
            additionalValue = 0,
            week = 1,
            mapName = "JC"
        )

        assertThat(result).isEqualTo(
            Resource.success(
                listOf(
                    BoxWithDropChance(
                        name = TextWithLocalization("test name 8 15", "тестовое имя 8 15"),
                        dropChance = 52.2,
                        mostLikelyGuard = 4,
                        range = 4..4,
                        type = "Unit",
                        img = "img3"
                    ),
                    BoxWithDropChance(
                        name = TextWithLocalization("item 7", "предмет 7"),
                        dropChance = 47.8,
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
            guardRange = 20..49,
            zoneType = 0,
            additionalValue = 0,
            week = 3,
            mapName = "JC"
        )

        assertThat(result).isEqualTo(
            Resource.success(
                listOf(
                    BoxWithDropChance(
                        name = TextWithLocalization("item 2", "предмет 2"),
                        dropChance = 85.0,
                        mostLikelyGuard = 41,
                        range = 31..49,
                        type = "Exp",
                        img = "img"
                    ),
                    BoxWithDropChance(
                        name = TextWithLocalization("test name 1 60", "тестовое имя 1 60"),
                        dropChance = 15.0,
                        mostLikelyGuard = 30,
                        range = 24..37,
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
            guardRange = 20..49,
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
            guardRange = 20..49,
            zoneType = 0,
            additionalValue = 5000,
            week = 1,
            mapName = "JC"
        )

        assertThat(result).isEqualTo(
            Resource.success(
                listOf(
                    BoxWithDropChance(
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
            guardRange = 20..49,
            zoneType = 0,
            additionalValue = 22000,
            week = 1,
            mapName = "JC"
        )
        assertThat(result).isEqualTo(Resource.error(msg = "It is impossible to find boxes for this kind or amount of guard", data = null))
    }

    @Test
    fun `Get Boxes, additional value and week correction, returns success`() = runTest {

        val guardUnit = Guard("testUnit", "тестЮнит", 190, 20, 30, "img")
        val result = getBox(
            guardUnit = guardUnit,
            castle = 1,
            castleZones = 1,
            guardRange = 20..49,
            zoneType = 0,
            additionalValue = 4000,
            week = 3,
            mapName = "JC"
        )
        assertThat(result).isEqualTo(
            Resource.success(
                listOf(
                    BoxWithDropChance(
                        name = TextWithLocalization("item 1", "предмет 1"),
                        dropChance = 100.0,
                        mostLikelyGuard = 49,
                        range = 45..49,
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
            guardRange = 20..49,
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
            castle = 10,
            castleZones = 1,
            guardRange = 20..49,
            zoneType = 0,
            additionalValue = 0,
            week = 1,
            mapName = "JC"
        )

        assertThat(result).isEqualTo(
            Resource.success(
                listOf(
                    BoxWithDropChance(
                        name = TextWithLocalization("item 2", "предмет 2"),
                        dropChance = 90.8,
                        mostLikelyGuard = 34,
                        range = 26..42,
                        type = "Exp",
                        img = "img"
                    ),
                    BoxWithDropChance(
                        name = TextWithLocalization("test name 4 45", "тестовое имя 4 45"),
                        dropChance = 8.9,
                        mostLikelyGuard = 46,
                        range = 35..49,
                        type = "Unit",
                        img = "img1"
                    ),
                    BoxWithDropChance(
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
            guardRange = 20..49,
            zoneType = 0,
            additionalValue = 0,
            week = 1,
            mapName = "JC"
        )

        assertThat(result).isEqualTo(
            Resource.success(
                listOf(
                    BoxWithDropChance(
                        name = TextWithLocalization("item 2", "предмет 2"),
                        dropChance = 87.0,
                        mostLikelyGuard = 34,
                        range = 26..42,
                        type = "Exp",
                        img = "img"
                    ),
                    BoxWithDropChance(
                        name = TextWithLocalization("test name 1 60", "тестовое имя 1 60"),
                        dropChance = 12.8,
                        mostLikelyGuard = 36,
                        range = 27..45,
                        type = "Unit",
                        img = "img1"
                    ),
                    BoxWithDropChance(
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
            guardRange = 20..49,
            zoneType = 0,
            additionalValue = 0,
            week = 1,
            mapName = "JC"
        )

        assertThat(result).isEqualTo(Resource.error(msg = "An unknown error occurred", data = null))
    }

    @Test
    fun `Get Boxes, exact guard, returns success`() = runTest {

        val guardUnit = Guard("testUnit", "тестЮнит", 190, 20, 30, "img")
        val result = getBox(
            guardUnit = guardUnit,
            castle = 1,
            castleZones = 1,
            guardRange = 28..28,
            zoneType = 0,
            additionalValue = 0,
            week = 1,
            mapName = "JC"
        )

        assertThat(result).isEqualTo(
            Resource.success(
                listOf(
                    BoxWithDropChance(
                        name = TextWithLocalization("item 2", "предмет 2"),
                        dropChance = 75.2,
                        mostLikelyGuard = 28,
                        range = 26..42,
                        type = "Exp",
                        img = "img"
                    ),
                    BoxWithDropChance(
                        name = TextWithLocalization("test name 1 60", "тестовое имя 1 60"),
                        dropChance = 24.8,
                        mostLikelyGuard = 28,
                        range = 19..31,
                        type = "Unit",
                        img = "img1"
                    )
                )
            )
        )
    }

    @Test
    fun `Get Boxes, exact guard, week correction, returns success`() = runTest {

        val guardUnit = Guard("testUnit", "тестЮнит", 190, 20, 30, "img")
        val result = getBox(
            guardUnit = guardUnit,
            castle = 1,
            castleZones = 1,
            guardRange = 28..28,
            zoneType = 0,
            additionalValue = 0,
            week = 2,
            mapName = "JC"
        )

        assertThat(result).isEqualTo(
            Resource.success(
                listOf(
                    BoxWithDropChance(
                        name = TextWithLocalization("test name 1 60", "тестовое имя 1 60"),
                        dropChance = 59.8,
                        mostLikelyGuard = 28,
                        range = 20..34,
                        type = "Unit",
                        img = "img1"
                    ),
                    BoxWithDropChance(
                        name = TextWithLocalization("item 2", "предмет 2"),
                        dropChance = 40.2,
                        mostLikelyGuard = 28,
                        range = 28..46,
                        type = "Exp",
                        img = "img"
                    )
                )
            )
        )
    }
}