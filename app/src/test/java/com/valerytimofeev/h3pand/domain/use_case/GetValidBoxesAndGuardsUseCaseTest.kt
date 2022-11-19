package com.valerytimofeev.h3pand.domain.use_case

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.valerytimofeev.h3pand.data.local.additional_data.Difficult
import com.valerytimofeev.h3pand.data.local.database.BoxValueItem
import com.valerytimofeev.h3pand.data.local.database.Guard
import com.valerytimofeev.h3pand.domain.model.BoxWithGuard
import com.valerytimofeev.h3pand.domain.model.GuardNumber
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
class GetValidBoxesAndGuardsUseCaseTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getValidBoxesAndGuards: GetValidBoxesAndGuardsUseCase


    @Before
    fun setup() {
        getValidBoxesAndGuards = GetValidBoxesAndGuardsUseCase()
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `Get valid boxes and guards, valid input`() = runTest {
        val boxes = listOf(
            BoxValueItem(1, "item 1", "предмет 1", 5000, img = "img", "Gold"),
            BoxValueItem(2, "item 2", "предмет 2", 7500, img = "img", "Exp"),
            BoxValueItem(
                id = 0,
                boxContent = "test name 1 60",
                boxContentRu = "тестовое имя 1 60",
                value = 5760,
                img = "img1",
                type = "Unit"
            )

        )
        val guard = Guard(
            name = "test name 1",
            nameRu = "тестовое имя 1",
            AIValue = 80,
            minOnMap = 20,
            maxOnMap = 50,
            img = "img1"
        )

        val result = getValidBoxesAndGuards(
            boxes = boxes,
            guard = guard,
            additionalValue = 0,
            difficult = Difficult.THREE,
            valueRange = 500..15000,
        )

        assertThat(result).isEqualTo(
            listOf(
                BoxWithGuard(
                    box = BoxValueItem(
                        id = 1,
                        boxContent = "item 1",
                        boxContentRu = "предмет 1",
                        value = 5000,
                        img = "img",
                        type = "Gold"
                    ),
                    guardNumber = GuardNumber(minGuard = 38, avgGuard = 50, maxGuard = 62)
                ),
                BoxWithGuard(
                    box = BoxValueItem(
                        id = 2,
                        boxContent = "item 2",
                        boxContentRu = "предмет 2",
                        value = 7500,
                        img = "img",
                        type = "Exp"
                    ),
                    guardNumber = GuardNumber(minGuard = 61, avgGuard = 81, maxGuard = 101)
                ),
                BoxWithGuard(
                    box = BoxValueItem(
                        id = 0,
                        boxContent = "test name 1 60",
                        boxContentRu = "тестовое имя 1 60",
                        value = 5760,
                        img = "img1",
                        type = "Unit"
                    ),
                    guardNumber = GuardNumber(minGuard = 45, avgGuard = 60, maxGuard = 75)
                )
            )
        )
    }

    @Test
    fun `Get valid boxes and guards, valid input, some guards above max number`() = runTest {
        val boxes = listOf(
            BoxValueItem(1, "item 1", "предмет 1", 5000, img = "img", "Gold"),
            BoxValueItem(2, "item 2", "предмет 2", 7500, img = "img", "Exp"),
            BoxValueItem(3, "item 3", "предмет 3", 10000, img = "img", "Spell"),
            BoxValueItem(4, "item 4", "предмет 4", 12500, img = "img", "Gold"),
            BoxValueItem(5, "item 5", "предмет 5", 15000, img = "img", "Exp"),
        )
        val guard = Guard(
            name = "test name 1",
            nameRu = "тестовое имя 1",
            AIValue = 80,
            minOnMap = 20,
            maxOnMap = 50,
            img = "img1"
        )

        val result = getValidBoxesAndGuards(
            boxes = boxes,
            guard = guard,
            additionalValue = 0,
            difficult = Difficult.THREE,
            valueRange = 500..15000,
        )

        assertThat(result.size).isEqualTo(2)
        assertThat(result.last()).isEqualTo(
            BoxWithGuard(
                box = BoxValueItem(
                    id = 2,
                    boxContent = "item 2",
                    boxContentRu = "предмет 2",
                    value = 7500,
                    img = "img",
                    type = "Exp"
                ),
                guardNumber = GuardNumber(minGuard = 61, avgGuard = 81, maxGuard = 101)
            )
        )
    }

    @Test
    fun `Get valid boxes and guards, valid input, some guards below min number`() = runTest {
        val boxes = listOf(
            BoxValueItem(1, "item 1", "предмет 1", 5000, img = "img", "Gold"),
            BoxValueItem(2, "item 2", "предмет 2", 7500, img = "img", "Exp"),
            BoxValueItem(3, "item 3", "предмет 3", 10000, img = "img", "Spell"),
            BoxValueItem(4, "item 4", "предмет 4", 12500, img = "img", "Gold"),
            BoxValueItem(5, "item 5", "предмет 5", 15000, img = "img", "Exp"),
        )
        val guard = Guard(
            name = "test name 3",
            nameRu = "тестовое имя 3",
            AIValue = 240,
            minOnMap = 16,
            maxOnMap = 25,
            img = "img1"
        )

        val result = getValidBoxesAndGuards(
            boxes = boxes,
            guard = guard,
            additionalValue = 0,
            difficult = Difficult.THREE,
            valueRange = 500..15000,
        )

        assertThat(result.size).isEqualTo(4)
        assertThat(result.first()).isEqualTo(
            BoxWithGuard(
                box = BoxValueItem(
                    id = 2,
                    boxContent = "item 2",
                    boxContentRu = "предмет 2",
                    value = 7500,
                    img = "img",
                    type = "Exp"
                ),
                guardNumber = GuardNumber(minGuard = 21, avgGuard = 27, maxGuard = 33)
            )
        )
    }

    @Test
    fun `Get valid boxes and guards, valid input, some boxes above value range`() = runTest {
        val boxes = listOf(
            BoxValueItem(1, "item 1", "предмет 1", 5000, img = "img", "Gold"),
            BoxValueItem(2, "item 2", "предмет 2", 7500, img = "img", "Exp"),
            BoxValueItem(3, "item 3", "предмет 3", 10000, img = "img", "Spell"),
        )
        val guard = Guard(
            name = "test name 1",
            nameRu = "тестовое имя 1",
            AIValue = 80,
            minOnMap = 20,
            maxOnMap = 50,
            img = "img1"
        )

        val result = getValidBoxesAndGuards(
            boxes = boxes,
            guard = guard,
            additionalValue = 0,
            difficult = Difficult.THREE,
            valueRange = 500..5000,
        )

        assertThat(result).isEqualTo(
            listOf(
                BoxWithGuard(
                    box = BoxValueItem(
                        id = 1,
                        boxContent = "item 1",
                        boxContentRu = "предмет 1",
                        value = 5000,
                        img = "img",
                        type = "Gold"
                    ),
                    guardNumber = GuardNumber(minGuard = 38, avgGuard = 50, maxGuard = 62)
                )
            )
        )
    }

    @Test
    fun `Get valid boxes and guards, all boxes above value range`() = runTest {
        val boxes = listOf(
            BoxValueItem(3, "item 3", "предмет 3", 10000, img = "img", "Spell"),
            BoxValueItem(4, "item 4", "предмет 4", 12500, img = "img", "Gold"),
            BoxValueItem(5, "item 5", "предмет 5", 15000, img = "img", "Exp"),
        )
        val guard = Guard(
            name = "test name 1",
            nameRu = "тестовое имя 1",
            AIValue = 80,
            minOnMap = 20,
            maxOnMap = 50,
            img = "img1"
        )

        val result = getValidBoxesAndGuards(
            boxes = boxes,
            guard = guard,
            additionalValue = 0,
            difficult = Difficult.THREE,
            valueRange = 500..5000,
        )

        assertThat(result).isEqualTo(emptyList<BoxWithGuard>())
    }

    @Test
    fun `Get valid boxes and guards, valid input, some boxes below value range`() = runTest {
        val boxes = listOf(
            BoxValueItem(1, "item 1", "предмет 1", 5000, img = "img", "Gold"),
            BoxValueItem(2, "item 2", "предмет 2", 7500, img = "img", "Exp"),
            BoxValueItem(3, "item 3", "предмет 3", 10000, img = "img", "Spell"),
            BoxValueItem(4, "item 4", "предмет 4", 12500, img = "img", "Gold"),
            BoxValueItem(5, "item 5", "предмет 5", 15000, img = "img", "Exp"),
        )
        val guard = Guard(
            name = "test name 3",
            nameRu = "тестовое имя 3",
            AIValue = 240,
            minOnMap = 16,
            maxOnMap = 25,
            img = "img1"
        )

        val result = getValidBoxesAndGuards(
            boxes = boxes,
            guard = guard,
            additionalValue = 0,
            difficult = Difficult.THREE,
            valueRange = 13000..15000,
        )

        assertThat(result.size).isEqualTo(1)
        assertThat(result.first()).isEqualTo(
            BoxWithGuard(
                box = BoxValueItem(
                    id = 5,
                    boxContent = "item 5",
                    boxContentRu = "предмет 5",
                    value = 15000,
                    img = "img",
                    type = "Exp"
                ),
                guardNumber = GuardNumber(minGuard = 68, avgGuard = 90, maxGuard = 112)
            )
        )
    }

    @Test
    fun `Get valid boxes and guards, all boxes below value range`() = runTest {
        val boxes = listOf(
            BoxValueItem(1, "item 1", "предмет 1", 5000, img = "img", "Gold"),
            BoxValueItem(2, "item 2", "предмет 2", 7500, img = "img", "Exp"),
            BoxValueItem(3, "item 3", "предмет 3", 10000, img = "img", "Spell"),
            BoxValueItem(4, "item 4", "предмет 4", 12500, img = "img", "Gold"),
            BoxValueItem(5, "item 5", "предмет 5", 15000, img = "img", "Exp"),
        )
        val guard = Guard(
            name = "test name 3",
            nameRu = "тестовое имя 3",
            AIValue = 240,
            minOnMap = 16,
            maxOnMap = 25,
            img = "img1"
        )

        val result = getValidBoxesAndGuards(
            boxes = boxes,
            guard = guard,
            additionalValue = 0,
            difficult = Difficult.THREE,
            valueRange = 20000..30000,
        )

        assertThat(result).isEqualTo(emptyList<BoxWithGuard>())
    }

    @Test
    fun `Get valid boxes and guards, different difficult`() = runTest {
        val boxes = listOf(
            BoxValueItem(1, "item 1", "предмет 1", 5000, img = "img", "Gold"),
            BoxValueItem(2, "item 2", "предмет 2", 7500, img = "img", "Exp"),
        )
        val guard = Guard(
            name = "test name 1",
            nameRu = "тестовое имя 1",
            AIValue = 80,
            minOnMap = 20,
            maxOnMap = 50,
            img = "img1"
        )

        val result = getValidBoxesAndGuards(
            boxes = boxes,
            guard = guard,
            additionalValue = 0,
            difficult = Difficult.FIVE,
            valueRange = 500..15000,
        )

        assertThat(result).isEqualTo(
            listOf(
                BoxWithGuard(
                    box = BoxValueItem(
                        id = 1,
                        boxContent = "item 1",
                        boxContentRu = "предмет 1",
                        value = 5000,
                        img = "img",
                        type = "Gold"
                    ),
                    guardNumber = GuardNumber(minGuard = 71, avgGuard = 94, maxGuard = 117)
                )
            )
        )
    }

    @Test
    fun `Get valid boxes and guards, additional value`() = runTest {
        val boxes = listOf(
            BoxValueItem(1, "item 1", "предмет 1", 5000, img = "img", "Gold"),
            BoxValueItem(2, "item 2", "предмет 2", 7500, img = "img", "Exp"),
        )
        val guard = Guard(
            name = "test name 1",
            nameRu = "тестовое имя 1",
            AIValue = 80,
            minOnMap = 20,
            maxOnMap = 50,
            img = "img1"
        )

        val result = getValidBoxesAndGuards(
            boxes = boxes,
            guard = guard,
            additionalValue = 2500,
            difficult = Difficult.THREE,
            valueRange = 500..15000,
        )

        assertThat(result).isEqualTo(
            listOf(
                BoxWithGuard(
                    box = BoxValueItem(
                        id = 1,
                        boxContent = "item 1",
                        boxContentRu = "предмет 1",
                        value = 5000,
                        img = "img",
                        type = "Gold"
                    ),
                    guardNumber = GuardNumber(minGuard = 61, avgGuard = 81, maxGuard = 101)
                )
            )
        )
    }


}