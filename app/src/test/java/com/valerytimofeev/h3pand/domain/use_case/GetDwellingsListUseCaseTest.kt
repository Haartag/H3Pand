package com.valerytimofeev.h3pand.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.valerytimofeev.h3pand.data.local.Dwelling
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
class GetDwellingsListUseCaseTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getDwellings: GetDwellingsListUseCase


    @Before
    fun setup() {
        getDwellings = GetDwellingsListUseCase(FakePandRepository())
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `Get dwellings, valid input`() = runTest {
        val result = getDwellings(3)

        Truth.assertThat(result.data).isEqualTo(
            listOf(
                Dwelling(
                    dwellingName = "test dwelling 3",
                    name = "test name 4",
                    AIValue = 160,
                    weeklyGain = 8,
                    castle = 3
                ),
                Dwelling(
                    dwellingName = "test dwelling 4",
                    name = "test name 5",
                    AIValue = 480,
                    weeklyGain = 4,
                    castle = 3
                ),
            )
        )
    }

    @Test
    fun `Get dwellings, invalid input`() = runTest {
        val result = getDwellings(20)

        Truth.assertThat(result).isEqualTo(
            Resource.error("An unknown database error occurred: database_5.0", null)
        )
    }
}