package com.valerytimofeev.h3pand.domain.use_case

import com.google.common.truth.Truth.assertThat
import com.valerytimofeev.h3pand.data.local.Guard
import com.valerytimofeev.h3pand.repositories.local.FakePandRepository
import com.valerytimofeev.h3pand.utils.BoxWithDropPercent
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

    @Before
    fun setup() {
        getBoxForGuardRange = GetBoxForGuardRangeUseCase(FakePandRepository())
        getGuardCharacteristicsUseCase = GetGuardCharacteristicsUseCase()
        getBoxWithPercentUseCase = GetBoxWithPercentUseCase()
        getBox = GetBoxesUseCase(
            getGuardCharacteristicsUseCase,
            getBoxForGuardRange,
            getBoxWithPercentUseCase
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

        val guardUnit = Guard("testUnit", 201, 20, 30, "img")
        val result = getBox.invoke(
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
            Resource.success(listOf<BoxWithDropPercent>(
                BoxWithDropPercent(name="item 2", dropChance=47.1, range=24..40, img="img"),
                BoxWithDropPercent(name="test name 1 80", dropChance=47.1, range=26..42, img="img1"),
                BoxWithDropPercent(name="item 3", dropChance=5.9, range=43..49, img="img")
            ))
        )
    }

    @Test
    fun `Get Boxes, valid input, 1-4 guard range, returns success`() = runTest {

        val guardUnit = Guard("testUnit", 6000, 3, 8, "img")
        val result = getBox.invoke(
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
            Resource.success(listOf<BoxWithDropPercent>(
                BoxWithDropPercent(name="item 7", dropChance=100.0, range=4..4, img="img")
            ))
        )
    }

    @Test
    fun `Get Boxes, valid input, week correction, returns success`() = runTest {

        val guardUnit = Guard("testUnit", 201, 20, 30, "img")
        val result = getBox.invoke(
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
            Resource.success(listOf<BoxWithDropPercent>(
                BoxWithDropPercent(name="item 2", dropChance=50.9, range=30..49, img="img"),
                BoxWithDropPercent(name="test name 1 80", dropChance=49.1, range=32..49, img="img1")
            ))
        )
    }

    @Test
    fun `Get Boxes, valid input, too big week correction, returns error`() = runTest {

        val guardUnit = Guard("testUnit", 201, 20, 30, "img")
        val result = getBox.invoke(
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

        val guardUnit = Guard("testUnit", 201, 20, 30, "img")
        val result = getBox.invoke(
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
            Resource.success(listOf<BoxWithDropPercent>(
                BoxWithDropPercent(name="item 1", dropChance=100.0, range=43..49, img="img"),
            ))
        )
    }

    @Test
    fun `Get Boxes, too big additional value, returns error`() = runTest {

        val guardUnit = Guard("testUnit", 201, 20, 30, "img")
        val result = getBox.invoke(
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

        val guardUnit = Guard("testUnit", 201, 20, 30, "img")
        val result = getBox.invoke(
            guardUnit = guardUnit,
            castle = 1,
            castleZones = 1,
            guardRangeIndex = 4,
            zoneType = 0,
            additionalValue = 3000,
            week = 3,
            mapName = "JC"
        )
        assertThat(result).isEqualTo(
            Resource.success(listOf<BoxWithDropPercent>(
                BoxWithDropPercent(name="item 1", dropChance=100.0, range=34..49, img="img"),
            ))
        )
    }

    @Test
    fun `Get Boxes, too big additional value and week correction, returns success`() = runTest {

        val guardUnit = Guard("testUnit", 201, 20, 30, "img")
        val result = getBox.invoke(
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

}