package com.valerytimofeev.h3pand.domain.use_case.dialog_use_case

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
class GetAdditionalValueListUseCaseTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getAdditionalValueListUseCase: GetAdditionalValueListUseCase
    private lateinit var fakePandRepository: FakePandRepository

    @Before
    fun setup() {
        fakePandRepository = FakePandRepository()
        getAdditionalValueListUseCase = GetAdditionalValueListUseCase(fakePandRepository)
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `Get addValue`() = runTest {
        val result = getAdditionalValueListUseCase()

        Truth.assertThat(result.data).isEqualTo(
            listOf("Custom value", "Misc.", "Artifact", "Dwelling")
        )
    }

    @Test
    fun `Get addValue, error`() = runTest {
        fakePandRepository.shouldReturnError(true)
        val result = getAdditionalValueListUseCase()
        Truth.assertThat(result).isEqualTo(
            Resource.error(
                "Error",
                null
            )
        )
    }

}