package com.valerytimofeev.h3pand.domain.use_case

import com.google.common.truth.Truth.assertThat
import com.valerytimofeev.h3pand.data.local.BoxValueItem
import com.valerytimofeev.h3pand.utils.*
import org.junit.Before
import org.junit.Test

class GetBoxWithPercentUseCaseTest() {

    private lateinit var getBoxWithPercentUseCase: GetBoxWithPercentUseCase

    @Before
    fun setup() {
        getBoxWithPercentUseCase = GetBoxWithPercentUseCase()
    }

    @Test
    fun `Get BoxWithDropPercent, valid input, all guards in range`() {
        val boxValueItem = BoxValueItem(1, "5000exp", 6000, "exp")
        val guardValue = GuardCharacteristics(16, 13, 65, 86)
        val difficult = Difficult.THREE
        val unitValue = 190
        val week = 1
        val additionalValue = 0
        val chosenGuardRange = 20..49

        val value = getBoxWithPercentUseCase(
            boxValueItem, guardValue, difficult, unitValue, week, chosenGuardRange, additionalValue
        )
        assertThat(value.data).isEqualTo(BoxWithDropPercent(name="5000exp", dropChance=100.0, range = 20..32, img="exp"))
    }

    @Test
    fun `Get BoxWithDropPercent, valid input, some guards not in range`() {
        val boxValueItem = BoxValueItem(1, "5000exp", 6000, "exp")
        //val guardValue = GuardCharacteristics(25, 4020, 65, 13065)
        val guardValue = GuardCharacteristics(30, 24, 65, 86)
        val difficult = Difficult.THREE
        val unitValue = 201
        val week = 1
        val additionalValue = 0
        val chosenGuardRange = 20..49

        val value = getBoxWithPercentUseCase(
            boxValueItem, guardValue, difficult, unitValue, week, chosenGuardRange, additionalValue
        )

        assertThat(value.data!!.dropChance).isAtLeast(69.0)
        assertThat(value.data!!.dropChance).isAtMost(69.5)
    }

    @Test
    fun `Get BoxWithDropPercent, too high value, returns error`() {
        val boxValueItem = BoxValueItem(1, "1LvlSpells", 5000, "1LvlSpells")
        val guardValue = GuardCharacteristics(16, 13, 65, 86)
        val difficult = Difficult.THREE
        val unitValue = 1350
        val week = 1
        val additionalValue = 0
        val chosenGuardRange = 20..49

        val value = getBoxWithPercentUseCase(
            boxValueItem, guardValue, difficult, unitValue, week, chosenGuardRange, additionalValue
        )

        assertThat(value).isEqualTo(Resource.error("An unknown error occurred", null))
    }

    @Test
    fun `Get BoxWithDropPercent, valid input, week after 1`() {
        val boxValueItem = BoxValueItem(1, "5000exp", 6000, "exp")
        val guardValue = GuardCharacteristics(16, 13, 65, 86)
        val difficult = Difficult.THREE
        val unitValue = 190
        val week = 3
        val additionalValue = 0
        val chosenGuardRange = 20..49

        val value = getBoxWithPercentUseCase(
            boxValueItem, guardValue, difficult, unitValue, week, chosenGuardRange, additionalValue
        )
        assertThat(value.data).isEqualTo(BoxWithDropPercent(name="5000exp", dropChance=100.0, range = 25..39, img="exp"))
    }

    @Test
    fun `!! Get BoxWithDropPercent, valid input, part of week-modified range above guard range`() {
        val boxValueItem = BoxValueItem(1, "5000exp", 6000, "exp")
        val guardValue = GuardCharacteristics(16, 13, 65, 86)
        val difficult = Difficult.THREE
        val unitValue = 190
        val week = 7
        val additionalValue = 0
        val chosenGuardRange = 20..49

        val value = getBoxWithPercentUseCase(
            boxValueItem, guardValue, difficult, unitValue, week, chosenGuardRange, additionalValue
        )
        assertThat(value.data!!.range).isEqualTo((36..49))
        assertThat(value.data!!.dropChance).isAtLeast(69.0)
        assertThat(value.data!!.dropChance).isAtMost(70.0)
    }

    @Test
    fun `!! Get BoxWithDropPercent, valid input, all week-modified range above guard range`() {
        val boxValueItem = BoxValueItem(1, "5000exp", 6000, "exp")
        val guardValue = GuardCharacteristics(16, 13, 65, 86)
        val difficult = Difficult.THREE
        val unitValue = 190
        val week = 11
        val additionalValue = 0
        val chosenGuardRange = 20..49

        val value = getBoxWithPercentUseCase(
            boxValueItem, guardValue, difficult, unitValue, week, chosenGuardRange, additionalValue
        )
        assertThat(value).isEqualTo(Resource.error("An unknown error occurred", null))
    }
}
