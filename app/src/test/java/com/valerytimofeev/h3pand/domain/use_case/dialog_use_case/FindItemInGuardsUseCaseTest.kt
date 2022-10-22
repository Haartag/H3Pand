package com.valerytimofeev.h3pand.domain.use_case.dialog_use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.valerytimofeev.h3pand.data.local.Guard
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
class FindItemInGuardsUseCaseTest() {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var findItem: FindItemInGuardsUseCase
    private lateinit var repository: FakePandRepository


    @Before
    fun setup() {
        repository = FakePandRepository()
        findItem = FindItemInGuardsUseCase(repository)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `Find item in Guard, valid input`() = runTest {
        val result = findItem("name")

        Truth.assertThat(result.data!!.size).isEqualTo(14)
        Truth.assertThat(result.data!![0]).isEqualTo(
            Guard(name = "test name 1", nameRu = "тестовое имя 1", AIValue = 80, minOnMap = 20, maxOnMap = 50, img = "img1")
            )
    }

    @Test
    fun `Find item in Guard, valid input, return 1 item`() = runTest {
        val result = findItem("2")

        Truth.assertThat(result.data).isEqualTo(
            listOf(
                Guard(name = "test name 2", nameRu = "тестовое имя 2", AIValue = 60, minOnMap = 20, maxOnMap = 50, img = "img1"),
            )
        )
    }

    @Test
    fun `Find item in Guard, valid input, return 0 item`() = runTest {
        val result = findItem("nothing")

        Truth.assertThat(result.data).isEqualTo(
            emptyList<Guard>()
        )
    }

    @Test
    fun `Find item in Guard, return error`() = runTest {
        repository.shouldReturnError(true)
        val result = findItem("nothing")

        Truth.assertThat(result).isEqualTo(
            Resource.error("Error", null)
        )
    }
}