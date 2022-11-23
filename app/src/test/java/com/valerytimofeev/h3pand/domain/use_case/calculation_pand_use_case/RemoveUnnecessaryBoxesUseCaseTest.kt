package com.valerytimofeev.h3pand.domain.use_case.calculation_pand_use_case

import com.google.common.truth.Truth.assertThat
import com.valerytimofeev.h3pand.data.local.database.BoxValueItem
import org.junit.Before
import org.junit.Test

class RemoveUnnecessaryBoxesUseCaseTest {

    private lateinit var removeUnnecessaryBoxes: RemoveUnnecessaryBoxesUseCase
    private var boxList = listOf(
    BoxValueItem(1, "item 1", "предмет 1", 5000, img = "img", "Gold"),
    BoxValueItem(2, "item 2", "предмет 2", 7500, img = "img", "Exp"),
    BoxValueItem(3, "item 3", "предмет 3", 10000, img = "img", "Spell"),
    BoxValueItem(4, "item 4", "предмет 4", 12500, img = "img", "Gold"),
    BoxValueItem(5, "item 5", "предмет 5", 15000, img = "img", "Exp"),
    BoxValueItem(6, "item 6", "предмет 6", 17500, img = "img", "Spell"),
    BoxValueItem(7, "item 7", "предмет 7", 20000, img = "img", "Gold"),
    )


    @Before
    fun setup() {
        removeUnnecessaryBoxes = RemoveUnnecessaryBoxesUseCase()

        boxList
    }

    @Test
    fun `Remove by type, valid input`() {
        val typesToBeRemoved = listOf("Exp", "Gold")
        val result = with(removeUnnecessaryBoxes) { boxList.removeByTypes(typesToBeRemoved) }
        assertThat(result).isEqualTo(
            listOf(
                BoxValueItem(3, "item 3", "предмет 3", 10000, img = "img", "Spell"),
                BoxValueItem(6, "item 6", "предмет 6", 17500, img = "img", "Spell"),
            )
        )
    }

    @Test
    fun `Remove by type, empty types list`() {
        val typesToBeRemoved = listOf<String>()
        val result = with(removeUnnecessaryBoxes) { boxList.removeByTypes(typesToBeRemoved) }
        assertThat(result).isEqualTo(boxList)
    }

    @Test
    fun `Remove by type, wrong types`() {
        val typesToBeRemoved = listOf("Test1", "Test2")
        val result = with(removeUnnecessaryBoxes) { boxList.removeByTypes(typesToBeRemoved) }
        assertThat(result).isEqualTo(boxList)
    }

    @Test
    fun `Remove by type, empty box list`() {
        val typesToBeRemoved = listOf("Exp", "Gold")
        val emptyList = listOf<BoxValueItem>()
        val result = with(removeUnnecessaryBoxes) { emptyList.removeByTypes(typesToBeRemoved) }
        assertThat(result).isEqualTo(emptyList)
    }

    @Test
    fun `Remove by id, valid input`() {
        val idsToBeRemoved = listOf(1, 5, 7)
        val result = with(removeUnnecessaryBoxes) { boxList.removeByIds(idsToBeRemoved) }
        assertThat(result).isEqualTo(
            listOf(
                BoxValueItem(2, "item 2", "предмет 2", 7500, img = "img", "Exp"),
                BoxValueItem(3, "item 3", "предмет 3", 10000, img = "img", "Spell"),
                BoxValueItem(4, "item 4", "предмет 4", 12500, img = "img", "Gold"),
                BoxValueItem(6, "item 6", "предмет 6", 17500, img = "img", "Spell"),
            )
        )
    }

    @Test
    fun `Remove by id, empty types list`() {
        val idsToBeRemoved = listOf<Int>()
        val result = with(removeUnnecessaryBoxes) { boxList.removeByIds(idsToBeRemoved) }
        assertThat(result).isEqualTo(boxList)
    }

    @Test
    fun `Remove by id, wrong types`() {
        val idsToBeRemoved = listOf(50, 100)
        val result = with(removeUnnecessaryBoxes) { boxList.removeByIds(idsToBeRemoved) }
        assertThat(result).isEqualTo(boxList)
    }

    @Test
    fun `Remove by id, empty box list`() {
        val idsToBeRemoved = listOf(1, 5, 7)
        val emptyList = listOf<BoxValueItem>()
        val result = with(removeUnnecessaryBoxes) { emptyList.removeByIds(idsToBeRemoved) }
        assertThat(result).isEqualTo(emptyList)
    }

}