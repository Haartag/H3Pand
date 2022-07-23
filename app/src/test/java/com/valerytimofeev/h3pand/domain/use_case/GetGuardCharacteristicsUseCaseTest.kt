package com.valerytimofeev.h3pand.domain.use_case

import com.google.common.truth.Truth.assertThat
import com.valerytimofeev.h3pand.utils.GuardCharacteristics
import com.valerytimofeev.h3pand.utils.Resource
import org.junit.Before
import org.junit.Test


class GetGuardCharacteristicsUseCaseTest {

    private lateinit var getGuardCharacteristicsUseCase: GetGuardCharacteristicsUseCase

    @Before
    fun setup() {
        getGuardCharacteristicsUseCase = GetGuardCharacteristicsUseCase()
    }


    @Test
    fun `Get GuardCharacteristics, valid import, returns success`() {

        val guardRangeIndex = 4
        val guardValue = 200
        val valueRange = 2000..22000
        val minGuardOnMap = 20

        val value = getGuardCharacteristicsUseCase(
            guardRangeIndex,
            guardValue,
            valueRange,
            minGuardOnMap
        )
        assertThat(value).isEqualTo(Resource.success(GuardCharacteristics(20, 4000, 49, 9800)))
    }

    @Test
    fun `Get GuardCharacteristics, valid import with correction, returns success`() {

        val guardRangeIndex = 4
        val guardValue = 200
        val valueRange = 2000..22000
        val minGuardOnMap = 20

        val value = getGuardCharacteristicsUseCase(
            guardRangeIndex,
            guardValue,
            valueRange,
            minGuardOnMap,
            true
        )
        assertThat(value).isEqualTo(Resource.success(GuardCharacteristics(15, 3000, 61, 12200)))
    }

    @Test
    fun `Get GuardCharacteristics, 1-4 unit, returns success`() {

        val guardRangeIndex = 1
        val guardValue = 25000
        val valueRange = 10000..55000
        val minGuardOnMap = 2

        val value = getGuardCharacteristicsUseCase(
            guardRangeIndex,
            guardValue,
            valueRange,
            minGuardOnMap
        )
        assertThat(value).isEqualTo(Resource.success(GuardCharacteristics(2, 50000, 2, 50000)))
    }

    @Test
    fun `Get GuardCharacteristics, 1-4 unit with correction, returns success`() {

        val guardRangeIndex = 1
        val guardValue = 25000
        val valueRange = 10000..55000
        val minGuardOnMap = 2

        val value = getGuardCharacteristicsUseCase(
            guardRangeIndex,
            guardValue,
            valueRange,
            minGuardOnMap,
            true
        )
        assertThat(value).isEqualTo(Resource.success(GuardCharacteristics(2, 50000, 2, 50000)))
    }

    @Test
    fun `Get GuardCharacteristics, some units under min threshold, returns success`() {

        val guardRangeIndex = 3
        val guardValue = 200
        val valueRange = 3000..22000
        val minGuardOnMap = 10

        val value = getGuardCharacteristicsUseCase(
            guardRangeIndex,
            guardValue,
            valueRange,
            minGuardOnMap
        )
        assertThat(value).isEqualTo(Resource.success(GuardCharacteristics(15, 3000, 19, 3800)))
    }

    @Test
    fun `Get GuardCharacteristics, some units above max threshold, returns success`() {

        val guardRangeIndex = 3
        val guardValue = 1500
        val valueRange = 3000..22000
        val minGuardOnMap = 7

        val value = getGuardCharacteristicsUseCase(
            guardRangeIndex,
            guardValue,
            valueRange,
            minGuardOnMap
        )
        assertThat(value).isEqualTo(Resource.success(GuardCharacteristics(10, 15000, 14, 21000)))
    }

    @Test
    fun `Get GuardCharacteristics, all units below min threshold, returns error`() {

        val guardRangeIndex = 2
        val guardValue = 270
        val valueRange = 3000..22000
        val minGuardOnMap = 18

        val value = getGuardCharacteristicsUseCase(
            guardRangeIndex,
            guardValue,
            valueRange,
            minGuardOnMap
        )
        assertThat(value).isEqualTo(Resource.error("Too weak unit", null))
    }

    @Test
    fun `Get GuardCharacteristics, all units above max threshold, returns error`() {

        val guardRangeIndex = 4
        val guardValue = 2100
        val valueRange = 3000..22000
        val minGuardOnMap = 7

        val value = getGuardCharacteristicsUseCase(
            guardRangeIndex,
            guardValue,
            valueRange,
            minGuardOnMap
        )
        assertThat(value).isEqualTo(Resource.error("Too strong unit", null))
    }
}