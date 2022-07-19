package com.valerytimofeev.h3pand.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class PandDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: PandDatabase
    private lateinit var dao: PandDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.pandDao()
    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun getAllGuardList_returnCorrectListSize() = runTest {
        val result = dao.getAllGuardsList()
        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun getAllAdditionalValuesList_returnCorrectListSize() = runTest {
        val result = dao.getAllAdditionalValuesList()
        assertThat(result.size).isEqualTo(3)
    }

    @Test
    fun getAllBoxValueItems_checkSize() = runTest {
        val minRange = 5000
        val maxRange = 8000
        val result = dao.getAllBoxesInRange(minRange, maxRange)
        val value = listOf(
            BoxValueItem(1, "5k gld", 5000, 0),
            BoxValueItem(2, "5k exp", 6000, 0)
        )
        assertThat(result).isEqualTo(value)
    }
}