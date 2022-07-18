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
    fun getGuardItemById() = runTest {
        val result = dao.getGuardById(2)
        assertThat(result).isEqualTo(GuardItem(2, "test2", 250))
    }

    @Test
    fun getAdditionalValueItemById() = runTest {
        val result = dao.getAdditionalValueById(2)
        assertThat(result).isEqualTo(AdditionalValueItem(2, "add2", 2000))
    }
}