package com.valerytimofeev.h3pand.domain.use_case

import com.google.common.truth.Truth.assertThat
import com.valerytimofeev.h3pand.data.local.additional_data.TextWithLocalization
import com.valerytimofeev.h3pand.domain.model.BoxWithDropChance
import org.junit.Before
import org.junit.Test


class GetItemListGroupsUseCaseTest() {

    private lateinit var getItemListGroupsUseCase: GetItemListGroupsUseCase

    @Before
    fun setup() {
        getItemListGroupsUseCase = GetItemListGroupsUseCase()
    }

    @Test
    fun `Make listGroups, valid input`() {
        val boxList = listOf(
            BoxWithDropChance(
                name = TextWithLocalization("item 2", "предмет 2"),
                dropChance = 85.2,
                mostLikelyGuard = 34,
                range = 26..42,
                type = "Exp",
                img = "img"
            ),
            BoxWithDropChance(
                name = TextWithLocalization("item 3", "предмет 3"),
                dropChance = 0.2,
                mostLikelyGuard = 49,
                range = 46..49,
                type = "Spell",
                img = "img"
            ),
            BoxWithDropChance(
                name = TextWithLocalization("test name 1 60", "тестовое имя 1 60"),
                dropChance = 14.5,
                mostLikelyGuard = 25,
                range = 20..31,
                type = "Unit",
                img = "img1"
            )
        )

        val result = getItemListGroupsUseCase(boxList)
        assertThat(result).isEqualTo(
            listOf(
                GetItemListGroupsUseCase.ListGroup(
                    type = "Exp",
                    img = "img",
                    summaryPercent = 85.2,
                    content = listOf(
                        BoxWithDropChance(
                            name = TextWithLocalization("item 2", "предмет 2"),
                            dropChance = 85.2,
                            mostLikelyGuard = 34,
                            range = 26..42,
                            type = "Exp",
                            img = "img"
                        )
                    )
                ),
                GetItemListGroupsUseCase.ListGroup(
                    type = "Spell",
                    img = "img",
                    summaryPercent = 0.2,
                    content = listOf(
                        BoxWithDropChance(
                            name = TextWithLocalization("item 3", "предмет 3"),
                            dropChance = 0.2,
                            mostLikelyGuard = 49,
                            range = 46..49,
                            type = "Spell",
                            img = "img"
                        )
                    )
                ),
                GetItemListGroupsUseCase.ListGroup(
                    type = "Unit",
                    img = "img1",
                    summaryPercent = 14.5,
                    content = listOf(
                        BoxWithDropChance(
                            name = TextWithLocalization("test name 1 60", "тестовое имя 1 60"),
                            dropChance = 14.5,
                            mostLikelyGuard = 25,
                            range = 20..31,
                            type = "Unit",
                            img = "img1"
                        )
                    )
                )
            )
        )
    }

    @Test
    fun `Make listGroups, only one group`() {
        val boxList = listOf(
            BoxWithDropChance(
                name = TextWithLocalization("item test 1", "предмет тест 1"),
                dropChance = 25.0,
                mostLikelyGuard = 34,
                range = 25..40,
                type = "Gold",
                img = "img"
            ),
            BoxWithDropChance(
                name = TextWithLocalization("item test 2", "предмет 3"),
                dropChance = 50.0,
                mostLikelyGuard = 49,
                range = 30..35,
                type = "Gold",
                img = "img"
            ),
            BoxWithDropChance(
                name = TextWithLocalization("item test 3", "тестовое имя 1 60"),
                dropChance = 25.0,
                mostLikelyGuard = 25,
                range = 20..31,
                type = "Gold",
                img = "img"
            )
        )

        val result = getItemListGroupsUseCase(boxList)

        assertThat(result.size).isEqualTo(1)
        assertThat(result[0].content.size).isEqualTo(3)
    }

}