package com.valerytimofeev.h3pand.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.valerytimofeev.h3pand.data.local.additional_data.MapSettings
import com.valerytimofeev.h3pand.data.local.additional_data.TextWithLocalization
import com.valerytimofeev.h3pand.data.local.additional_data.ValueRange
import com.valerytimofeev.h3pand.data.local.additional_data.ZoneSettings
import com.valerytimofeev.h3pand.data.local.database.BoxValueItem
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
class GetUnitDropCoefficientUseCaseTest() {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getUnitDropCoefficientUseCase: GetUnitDropCoefficientUseCase


    @Before
    fun setup() {
        getUnitDropCoefficientUseCase = GetUnitDropCoefficientUseCase(FakePandRepository())
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
    fun `Get drop coefficient, first zoneType, first range`() = runTest {
        val boxValueItem = BoxValueItem(
            1,
            "item 1",
            "предмет 1",
            5000,
            img = "img",
            "Gold"
        )

        val result = getUnitDropCoefficientUseCase(
            boxValueItem,
            castle = 1,
            numberOfZones = 5.0f,
            numberOfUnitZones = 1.0f,
            mapSettings = MapSettings.JC,
            zone = 0,
            exactlyGuard = false
        )

        Truth.assertThat(result).isEqualTo(Resource.success(0.033584908f))
    }

    @Test
    fun `Get drop coefficient, different type`() = runTest {
        val boxValueItem = BoxValueItem(
            1,
            "item 1",
            "предмет 1",
            5000,
            img = "img",
            "Exp"
        )

        val result = getUnitDropCoefficientUseCase(
            boxValueItem,
            castle = 1,
            numberOfZones = 5.0f,
            numberOfUnitZones = 1.0f,
            mapSettings = MapSettings.JC,
            zone = 0,
            exactlyGuard = false
        )

        Truth.assertThat(result).isEqualTo(Resource.success(0.13433963f))
    }

    @Test
    fun `Get drop coefficient, different castle`() = runTest {
        val boxValueItem = BoxValueItem(
            1,
            "item 1",
            "предмет 1",
            5000,
            img = "img",
            "Exp"
        )

        val result = getUnitDropCoefficientUseCase(
            boxValueItem,
            castle = 10,
            numberOfZones = 5.0f,
            numberOfUnitZones = 1.0f,
            mapSettings = MapSettings.JC,
            zone = 0,
            exactlyGuard = false
        )

        Truth.assertThat(result).isEqualTo(Resource.success(0.1277193f))
    }

    @Test
    fun `Get drop coefficient, second zoneType, first range`() = runTest {
        val boxValueItem = BoxValueItem(
            7,
            "item 7",
            "предмет 7",
            20000,
            img = "img",
            "Gold"
        )

        val result = getUnitDropCoefficientUseCase(
            boxValueItem,
            castle = 1,
            numberOfZones = 5.0f,
            numberOfUnitZones = 1.0f,
            mapSettings = MapSettings.JC,
            zone = 1,
            exactlyGuard = false
        )

        Truth.assertThat(result).isEqualTo(Resource.success(0.084535286f))
    }

    @Test
    fun `Get drop coefficient, wrong zoneType, return error`() = runTest {
        val boxValueItem = BoxValueItem(
            1,
            "item 1",
            "предмет 1",
            5000,
            img = "img",
            "Gold"
        )

        val result = getUnitDropCoefficientUseCase(
            boxValueItem,
            castle = 1,
            numberOfZones = 5.0f,
            numberOfUnitZones = 1.0f,
            mapSettings = MapSettings.JC,
            zone = 10,
            exactlyGuard = false
        )

        Truth.assertThat(result).isEqualTo(Resource.error("Error: Wrong map settings", null))
    }

    @Test
    fun `Get drop coefficient, box value below zone valueRange`() = runTest {
        val boxValueItem = BoxValueItem(
            1,
            "item 1",
            "предмет 1",
            5000,
            img = "img",
            "Gold"
        )

        val result = getUnitDropCoefficientUseCase(
            boxValueItem,
            castle = 1,
            numberOfZones = 5.0f,
            numberOfUnitZones = 1.0f,
            mapSettings = MapSettings.JC,
            zone = 1,
            exactlyGuard = false
        )

        Truth.assertThat(result).isEqualTo(Resource.success(0.18193926f))
    }

    @Test
    fun `Get drop coefficient, box value above zone valueRange, returns error`() = runTest {
        val boxValueItem = BoxValueItem(
            1,
            "item test",
            "предмет тест",
            50000,
            img = "img",
            "Gold"
        )

        val result = getUnitDropCoefficientUseCase(
            boxValueItem,
            castle = 1,
            numberOfZones = 5.0f,
            numberOfUnitZones = 1.0f,
            mapSettings = MapSettings.JC,
            zone = 1,
            exactlyGuard = false
        )

        Truth.assertThat(result).isEqualTo(Resource.error("Error: Wrong map settings", null))
    }

    @Test
    fun `Get drop coefficient, wrong number of zones, returns error`() = runTest {
        val boxValueItem = BoxValueItem(
            1,
            "item test",
            "предмет тест",
            5000,
            img = "img",
            "Gold"
        )

        val result = getUnitDropCoefficientUseCase(
            boxValueItem,
            castle = 1,
            numberOfZones = 5.0f,
            numberOfUnitZones = 10.0f,
            mapSettings = MapSettings.JC,
            zone = 0,
            exactlyGuard = false
        )

        Truth.assertThat(result).isEqualTo(Resource.error("An unknown error occurred", null))
    }

    @Test
    fun `Get drop coefficient, exactly guard`() = runTest {
        val boxValueItem = BoxValueItem(
            1,
            "item 1",
            "предмет 1",
            5000,
            img = "img",
            "Gold"
        )

        val result = getUnitDropCoefficientUseCase(
            boxValueItem,
            castle = 1,
            numberOfZones = 5.0f,
            numberOfUnitZones = 1.0f,
            mapSettings = MapSettings.JC,
            zone = 0,
            exactlyGuard = true
        )

        Truth.assertThat(result).isEqualTo(Resource.success(15.0f))
    }

    @Test
    fun `Get drop coefficient, exactly guard, different type`() = runTest {
        val boxValueItem = BoxValueItem(
            1,
            "item 1",
            "предмет 1",
            5000,
            img = "img",
            "Exp"
        )

        val result = getUnitDropCoefficientUseCase(
            boxValueItem,
            castle = 1,
            numberOfZones = 5.0f,
            numberOfUnitZones = 1.0f,
            mapSettings = MapSettings.JC,
            zone = 0,
            exactlyGuard = true
        )

        Truth.assertThat(result).isEqualTo(Resource.success(60.0f))
    }

    @Test
    fun `Get drop coefficient, exactly guard, different castle`() = runTest {
        val boxValueItem = BoxValueItem(
            1,
            "item 1",
            "предмет 1",
            5000,
            img = "img",
            "Gold"
        )

        val result = getUnitDropCoefficientUseCase(
            boxValueItem,
            castle = 10,
            numberOfZones = 5.0f,
            numberOfUnitZones = 1.0f,
            mapSettings = MapSettings.JC,
            zone = 0,
            exactlyGuard = true
        )

        Truth.assertThat(result).isEqualTo(Resource.success(15.0f))
    }
}