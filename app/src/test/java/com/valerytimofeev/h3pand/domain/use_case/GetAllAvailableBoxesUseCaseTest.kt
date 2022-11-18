package com.valerytimofeev.h3pand.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.valerytimofeev.h3pand.data.local.database.BoxValueItem
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
class GetAllAvailableBoxesUseCaseTest() {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getAllAvailableBoxes: GetAllAvailableBoxesUseCase


    @Before
    fun setup() {
        getAllAvailableBoxes = GetAllAvailableBoxesUseCase(FakePandRepository())
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `Get all boxes, valid input, return success`() = runTest {

        val result = getAllAvailableBoxes(
            castle = 1,
            castleZones = 1,
            zones = 5
        )
        assertThat(result.data!!.size).isEqualTo(8)
        assertThat(result.data!![1]).isEqualTo(
            BoxValueItem(
                id = 2,
                boxContent = "item 2",
                boxContentRu = "предмет 2",
                value = 7500,
                img = "img",
                type = "Exp"
            )
        )
    }

    @Test
    fun `Get all boxes, different castle, return success`() = runTest {

        val result = getAllAvailableBoxes(
            castle = 9,
            castleZones = 1,
            zones = 5
        )
        assertThat(result.data!!.size).isEqualTo(9)
    }

    @Test
    fun `Get all boxes, wrong castle, returns error`() = runTest {

        val result = getAllAvailableBoxes(
            castle = 20,
            castleZones = 1,
            zones = 5
        )
        assertThat(result).isEqualTo(Resource.error("An unknown error occurred", null))
    }

    @Test
    fun `Get all boxes, wrong zones, returns error`() = runTest {

        val result = getAllAvailableBoxes(
            castle = 20,
            castleZones = 10,
            zones = 5
        )
        assertThat(result).isEqualTo(Resource.error("An unknown error occurred", null))
    }
}