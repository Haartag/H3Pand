package com.valerytimofeev.h3pand.domain.use_case.dialog_use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.valerytimofeev.h3pand.domain.model.SearchItem
import com.valerytimofeev.h3pand.domain.use_case.GetDwellingsListUseCase
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
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class FindItemInAdditionalValuesUseCaseTest {


    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var findItem: FindItemInAdditionalValuesUseCase
    private lateinit var getDwellings: GetDwellingsListUseCase
    private lateinit var convert: ConvertToSearchListUseCase


    @Before
    fun setup() {
        getDwellings = GetDwellingsListUseCase(FakePandRepository())
        convert = ConvertToSearchListUseCase()
        findItem = FindItemInAdditionalValuesUseCase(FakePandRepository(), getDwellings, convert)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `Find item in AdditionalValue, valid input, result from both sources`() = runTest {
        val result = findItem("1", 1)

        Truth.assertThat(result.data).isEqualTo(
            listOf(                SearchItem(
                itemName = "add name 1",
                isDwelling = false,
                addItemValue = 1400,
                unitValue = null,
                weeklyGain = null,
                castle = null
            ),
                SearchItem(
                    itemName = "test dwelling 1",
                    isDwelling = true,
                    addItemValue = null,
                    unitValue = 80,
                    weeklyGain = 15,
                    castle = 1
                ),)
        )
    }

    @Test
    fun `Find item in AdditionalValue, valid input, result from addValue`() = runTest {
        val result = findItem("name", 1)

        Truth.assertThat(result.data!!.map { it.itemName }).isEqualTo(
            listOf("add name 1", "add name 2", "add name 3")
        )
    }

    @Test
    fun `Find item in AdditionalValue, valid input, result from guard`() = runTest {
        val result = findItem("dwelling", 5)

        Truth.assertThat(result.data!!.map { it.itemName }).isEqualTo(
            listOf("test dwelling 3")
        )
    }

    @Test
    fun `Find item in AdditionalValue, invalid search input`() = runTest {
        val result = findItem("something", 1)

        Truth.assertThat(result).isEqualTo(
            Resource.success(listOf<String>())
        )
    }

    @Test
    fun `Find item in AdditionalValue, invalid castle`() = runTest {
        val result = findItem("1", 20)

        Truth.assertThat(result).isEqualTo(
            Resource.error("An unknown database error occurred: database_5.0", null)
        )
    }


}