package com.valerytimofeev.h3pand.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
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


    @Before
    fun setup() {
        getDwellings = GetDwellingsListUseCase(FakePandRepository())
        findItem = FindItemInAdditionalValuesUseCase(FakePandRepository(), getDwellings)
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
            listOf("add name 1", "test dwelling 1")
        )
    }

    @Test
    fun `Find item in AdditionalValue, valid input, result from addValue`() = runTest {
        val result = findItem("name", 1)

        Truth.assertThat(result.data).isEqualTo(
            listOf("add name 1", "add name 2", "add name 3")
        )
    }

    @Test
    fun `Find item in AdditionalValue, valid input, result from guard`() = runTest {
        val result = findItem("dwelling", 5)

        Truth.assertThat(result.data).isEqualTo(
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
            Resource.error("Search error", null)
        )
    }

}