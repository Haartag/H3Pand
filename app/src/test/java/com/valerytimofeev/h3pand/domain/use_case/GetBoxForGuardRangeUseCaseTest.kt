package com.valerytimofeev.h3pand.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.valerytimofeev.h3pand.data.local.BoxValueItem
import com.valerytimofeev.h3pand.repositories.local.FakePandRepository
import com.valerytimofeev.h3pand.utils.Difficult
import com.valerytimofeev.h3pand.utils.GuardCharacteristics
import com.valerytimofeev.h3pand.utils.Resource
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
class GetBoxForGuardRangeUseCaseTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getBox: GetBoxForGuardRangeUseCase

    @Before
    fun setup() {
        getBox = GetBoxForGuardRangeUseCase(FakePandRepository())
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `Get box list, valid input`() = runTest {
        val guardRange = GuardCharacteristics(20, 4020, 65, 13065)
        val difficult = Difficult.THREE

        val result = getBox(guardRange, difficult, 0, 1, 5, 1)

        assertThat(result).isEqualTo(
            Resource.success(
                listOf(
                    BoxValueItem(id = 2, boxContent = "item 2", value = 7500, img = "img"),
                    BoxValueItem(id = 3, boxContent = "item 3", value = 10000, img = "img"),
                    BoxValueItem(id = 0, boxContent = "test name 1 80", value = 7680, img = "img")
                )
            )
        )
    }

    @Test
    fun `Get box list, valid input, castle 3`() = runTest {
        val guardRange = GuardCharacteristics(20, 4020, 65, 13065)
        val difficult = Difficult.THREE

        val result = getBox(guardRange, difficult, 0, 1, 5, 3)

        assertThat(result).isEqualTo(
            Resource.success(
                listOf(
                    BoxValueItem(id = 2, boxContent = "item 2", value = 7500, img = "img"),
                    BoxValueItem(id = 3, boxContent = "item 3", value = 10000, img = "img"),
                    BoxValueItem(id=0, boxContent="test name 2 30", value=9000, img="img")
                )
            )
        )
    }

    @Test
    fun `Get box list, valid input, empty unit box list`() = runTest {
        val guardRange = GuardCharacteristics(20, 4020, 65, 13065)
        val difficult = Difficult.THREE

        val repository = FakePandRepository()
        repository.shouldReturnEmptyUnitBoxList(true)
        getBox = GetBoxForGuardRangeUseCase(repository)

        val result = getBox(guardRange, difficult, 0, 1, 5, 1)

        assertThat(result).isEqualTo(
            Resource.success(
                listOf(
                    BoxValueItem(id = 2, boxContent = "item 2", value = 7500, img = "img"),
                    BoxValueItem(id = 3, boxContent = "item 3", value = 10000, img = "img")
                )
            )
        )
    }

    @Test
    fun `Get box list, valid input, empty non-unit box list`() = runTest {
        val guardRange = GuardCharacteristics(20, 4020, 65, 13065)
        val difficult = Difficult.THREE

        val repository = FakePandRepository()
        repository.shouldReturnEmptyNonUnitBoxList(true)
        getBox = GetBoxForGuardRangeUseCase(repository)

        val result = getBox(guardRange, difficult, 0, 1, 5, 1)

        assertThat(result).isEqualTo(
            Resource.success(
                listOf(
                    BoxValueItem(id = 0, boxContent = "test name 1 80", value = 7680, img = "img")
                )
            )
        )
    }

    @Test
    fun `Get box list,  empty non-unit box list and unit box list`() = runTest {
        val guardRange = GuardCharacteristics(16, 32000, 65, 130000)
        val difficult = Difficult.THREE

        val result = getBox(guardRange, difficult, 0, 1, 5, 1)

        assertThat(result).isEqualTo(Resource.error("No boxes found", null))
    }

    @Test
    fun `Get box list, valid input, with additional value, empty unit box list`() = runTest {
        val guardRange = GuardCharacteristics(20, 4020, 65, 13065)
        val difficult = Difficult.THREE
        val additionalValue = 4000

        val result = getBox(guardRange, difficult, additionalValue, 1, 5, 1).data

        assertThat(result).isEqualTo(
            listOf(
                BoxValueItem(id = 1, boxContent = "item 1", value = 5000, img = "img")
            )
        )
    }

    @Test
    fun `Get box list, valid input, with additional value, empty non-unit box list`() = runTest {
        val guardRange = GuardCharacteristics(50, 10050, 65, 13065)
        val difficult = Difficult.THREE
        val additionalValue = 1000

        val result = getBox(guardRange, difficult, additionalValue, 1, 5, 3).data

        assertThat(result).isEqualTo(
            listOf(
                BoxValueItem(id=0, boxContent="test name 2 30", value=9000, img="img")
            )
        )
    }

    @Test
    fun `Get box list, valid input, with 2 castle zones in 5 zones`() = runTest {
        val guardRange = GuardCharacteristics(20, 4020, 65, 13065)
        val difficult = Difficult.THREE

        val repository = FakePandRepository()
        repository.shouldReturnEmptyNonUnitBoxList(true)
        getBox = GetBoxForGuardRangeUseCase(repository)

        val result = getBox(guardRange, difficult, 0, 2, 5, 1)

        assertThat(result).isEqualTo(
            Resource.success(
                listOf(
                    BoxValueItem(id = 0, boxContent = "test name 1 80", value = 8960, img = "img")
                )
            )
        )
    }

    @Test
    fun `Get box list, valid input, with 10 castle zones in 10 zones`() = runTest {
        val guardRange = GuardCharacteristics(20, 4020, 99, 19899)
        val difficult = Difficult.THREE

        val repository = FakePandRepository()
        repository.shouldReturnEmptyNonUnitBoxList(true)
        getBox = GetBoxForGuardRangeUseCase(repository)

        val result = getBox(guardRange, difficult, 0, 10, 10, 1)

        assertThat(result).isEqualTo(
            Resource.success(
                listOf(
                    BoxValueItem(id = 0, boxContent = "test name 1 80", value = 12800, img = "img")
                )
            )
        )
    }

    @Test
    fun `Get box list, valid input, too big additional value`() = runTest {
        val guardRange = GuardCharacteristics(20, 4020, 65, 13065)
        val difficult = Difficult.THREE
        val additionalValue = 40000

        val result = getBox(guardRange, difficult, additionalValue, 1, 5, 1)

        assertThat(result).isEqualTo(Resource.error("No boxes found", null))
    }
}