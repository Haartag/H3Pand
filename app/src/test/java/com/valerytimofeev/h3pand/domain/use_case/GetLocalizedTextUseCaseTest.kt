package com.valerytimofeev.h3pand.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.valerytimofeev.h3pand.data.local.additional_data.TextWithLocalization
import com.valerytimofeev.h3pand.data.local.database.AdditionalValueItem
import com.valerytimofeev.h3pand.data.local.database.Dwelling
import com.valerytimofeev.h3pand.data.local.database.Guard
import com.valerytimofeev.h3pand.domain.model.CurrentLocal
import com.valerytimofeev.h3pand.domain.model.SearchItem
import io.mockk.every
import io.mockk.mockk
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
class GetLocalizedTextUseCaseTest() {


    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getLocalizedText: GetLocalizedTextUseCase


    @Before
    fun setup() {
        getLocalizedText = GetLocalizedTextUseCase()
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }


    @Test
    fun `Get localized text, TextWithLocalization input, en localization`() = runTest {
        val localization = 0
        val text = TextWithLocalization("TestEn", "ТестРу")

        val mock = mockk<CurrentLocal>()
        every { mock.local } returns (localization)

        val result = getLocalizedText(text)
        assertThat(result).isEqualTo("TestEn")
    }

    @Test
    fun `Get localized text, TextWithLocalization input, ru localization`() = runTest {
        val localization = 1
        val text = TextWithLocalization("TestEn", "ТестРу")

        val mock = mockk<CurrentLocal>()
        every { mock.local } returns (localization)

        val result = getLocalizedText(text)
        assertThat(result).isEqualTo("ТестРу")
    }

    @Test
    fun `Get localized text, TextWithLocalization input, wrong localization`() = runTest {
        val localization = 10
        val text = TextWithLocalization("TestEn", "ТестРу")

        val mock = mockk<CurrentLocal>()
        every { mock.local } returns (localization)

        val result = getLocalizedText(text)
        assertThat(result).isEmpty()
    }

    @Test
    fun `Get localized text, Guard input, en localization`() = runTest {
        val localization = 0
        val text = Guard(
            "testUnit",
            "тестЮнит",
            190,
            20,
            30,
            "img"
        )

        val mock = mockk<CurrentLocal>()
        every { mock.local } returns (localization)

        val result = getLocalizedText(text)
        assertThat(result).isEqualTo("testUnit")
    }

    @Test
    fun `Get localized text, Guard input, ru localization`() = runTest {
        val localization = 1
        val text = Guard(
            "testUnit",
            "тестЮнит",
            190,
            20,
            30,
            "img"
        )

        val mock = mockk<CurrentLocal>()
        every { mock.local } returns (localization)

        val result = getLocalizedText(text)
        assertThat(result).isEqualTo("тестЮнит")
    }

    @Test
    fun `Get localized text, Guard input, wrong localization`() = runTest {
        val localization = 10
        val text = Guard(
            "testUnit",
            "тестЮнит",
            190,
            20,
            30,
            "img"
        )

        val mock = mockk<CurrentLocal>()
        every { mock.local } returns (localization)

        val result = getLocalizedText(text)
        assertThat(result).isEmpty()
    }

    @Test
    fun `Get localized text, null input`() = runTest {
        val localization = 0
        val text = null

        val mock = mockk<CurrentLocal>()
        every { mock.local } returns (localization)

        val result = getLocalizedText(text)
        assertThat(result).isEmpty()
    }

    @Test
    fun `Get localized text, Dwelling input, en localization`() = runTest {
        val localization = 0
        val text = Dwelling(
            dwellingName = "test dwelling 3",
            dwellingNameRu = "тестовое жилище 3",
            name = "test name 4",
            nameRu = "тестовое имя 4",
            AIValue = 160,
            weeklyGain = 8,
            castle = 3
        )

        val mock = mockk<CurrentLocal>()
        every { mock.local } returns (localization)

        val result = getLocalizedText(text)
        assertThat(result).isEqualTo("test dwelling 3")
    }

    @Test
    fun `Get localized text, Dwelling input, ru localization`() = runTest {
        val localization = 1
        val text = Dwelling(
            dwellingName = "test dwelling 3",
            dwellingNameRu = "тестовое жилище 3",
            name = "test name 4",
            nameRu = "тестовое имя 4",
            AIValue = 160,
            weeklyGain = 8,
            castle = 3
        )

        val mock = mockk<CurrentLocal>()
        every { mock.local } returns (localization)

        val result = getLocalizedText(text)
        assertThat(result).isEqualTo("тестовое жилище 3")
    }

    @Test
    fun `Get localized text, Dwelling input, wrong localization`() = runTest {
        val localization = 10
        val text = Dwelling(
            dwellingName = "test dwelling 3",
            dwellingNameRu = "тестовое жилище 3",
            name = "test name 4",
            nameRu = "тестовое имя 4",
            AIValue = 160,
            weeklyGain = 8,
            castle = 3
        )

        val mock = mockk<CurrentLocal>()
        every { mock.local } returns (localization)

        val result = getLocalizedText(text)
        assertThat(result).isEmpty()
    }

    @Test
    fun `Get localized text, AdditionalValueItem input, en localization`() = runTest {
        val localization = 0
        val text = AdditionalValueItem(
            3,
            "add name 3",
            "доп. имя 3",
            5000,
            100,
            "Misc.",
            "Разное",
            "Trade",
            "Торговля",
            0
        )

        val mock = mockk<CurrentLocal>()
        every { mock.local } returns (localization)

        val result = getLocalizedText(text)
        assertThat(result).isEqualTo("add name 3")
    }

    @Test
    fun `Get localized text, AdditionalValueItem input, ru localization`() = runTest {
        val localization = 1
        val text = AdditionalValueItem(
            3,
            "add name 3",
            "доп. имя 3",
            5000,
            100,
            "Misc.",
            "Разное",
            "Trade",
            "Торговля",
            0
        )

        val mock = mockk<CurrentLocal>()
        every { mock.local } returns (localization)

        val result = getLocalizedText(text)
        assertThat(result).isEqualTo("доп. имя 3")
    }

    @Test
    fun `Get localized text, AdditionalValueItem input, wrong localization`() = runTest {
        val localization = 10
        val text = AdditionalValueItem(
            3,
            "add name 3",
            "доп. имя 3",
            5000,
            100,
            "Misc.",
            "Разное",
            "Trade",
            "Торговля",
            0
        )

        val mock = mockk<CurrentLocal>()
        every { mock.local } returns (localization)

        val result = getLocalizedText(text)
        assertThat(result).isEmpty()
    }

    @Test
    fun `Get localized text, SearchItem input, en localization`() = runTest {
        val localization = 0
        val text = SearchItem(
            itemName = "add name 1",
            itemNameRu = "доп. имя 1",
            isDwelling = false,
            addItemValue = 1400,
            unitValue = null,
            weeklyGain = null,
            castle = null
        )

        val mock = mockk<CurrentLocal>()
        every { mock.local } returns (localization)

        val result = getLocalizedText(text)
        assertThat(result).isEqualTo("add name 1")
    }

    @Test
    fun `Get localized text, SearchItem input, ru localization`() = runTest {
        val localization = 1
        val text = SearchItem(
            itemName = "add name 1",
            itemNameRu = "доп. имя 1",
            isDwelling = false,
            addItemValue = 1400,
            unitValue = null,
            weeklyGain = null,
            castle = null
        )

        val mock = mockk<CurrentLocal>()
        every { mock.local } returns (localization)

        val result = getLocalizedText(text)
        assertThat(result).isEqualTo("доп. имя 1")
    }

    @Test
    fun `Get localized text, SearchItem input, wrong localization`() = runTest {
        val localization = 10
        val text = SearchItem(
            itemName = "add name 1",
            itemNameRu = "доп. имя 1",
            isDwelling = false,
            addItemValue = 1400,
            unitValue = null,
            weeklyGain = null,
            castle = null
        )

        val mock = mockk<CurrentLocal>()
        every { mock.local } returns (localization)

        val result = getLocalizedText(text)
        assertThat(result).isEmpty()
    }
}