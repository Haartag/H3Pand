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
        val guardValue = 201
        val minGuardOnMap = 20
        val maxGuardOnMap = 99

        val value = getGuardCharacteristicsUseCase(
            guardRangeIndex,
            guardValue,
            minGuardOnMap,
            maxGuardOnMap
        )
        assertThat(value).isEqualTo(Resource.success(GuardCharacteristics(20, 4020, 65, 13065)))
    }

    @Test
    fun `Get GuardCharacteristics, 1-4 unit, returns success`() {

        val guardRangeIndex = 1
        val guardValue = 25000
        val minGuardOnMap = 2
        val maxGuardOnMap = 99

        val value = getGuardCharacteristicsUseCase(
            guardRangeIndex,
            guardValue,
            minGuardOnMap,
            maxGuardOnMap
        )
        assertThat(value).isEqualTo(Resource.success(GuardCharacteristics(2, 50000, 5, 125000)))
    }

    @Test
    fun `Get GuardCharacteristics, range under min guards, returns success`() {

        val guardRangeIndex = 3
        val guardValue = 201
        val minGuardOnMap = 20
        val maxGuardOnMap = 99

        val value = getGuardCharacteristicsUseCase(
            guardRangeIndex,
            guardValue,
            minGuardOnMap,
            maxGuardOnMap
        )
        assertThat(value).isEqualTo(Resource.success(GuardCharacteristics(20, 4020, 25, 5025)))
    }

    @Test
    fun `Get GuardCharacteristics, range above max guards, returns success`() {

        val guardRangeIndex = 6
        val guardValue = 201
        val minGuardOnMap = 20
        val maxGuardOnMap = 99

        val value = getGuardCharacteristicsUseCase(
            guardRangeIndex,
            guardValue,
            minGuardOnMap,
            maxGuardOnMap
        )
        assertThat(value).isEqualTo(Resource.success(GuardCharacteristics(80, 16080, 99, 19899)))
    }

    @Test
    fun `Get GuardCharacteristics, all units below min threshold, returns error`() {

        val guardRangeIndex = 2
        val guardValue = 270
        val minGuardOnMap = 18
        val maxGuardOnMap = 99

        val value = getGuardCharacteristicsUseCase(
            guardRangeIndex,
            guardValue,
            minGuardOnMap,
            maxGuardOnMap
        )
        assertThat(value).isEqualTo(Resource.error("Too weak unit", null))
    }

    @Test
    fun `Get GuardCharacteristics, valid import, week correction, returns success`() {

        val guardRangeIndex = 4
        val guardValue = 201
        val minGuardOnMap = 20
        val maxGuardOnMap = 99
        val week = 2

        val value = getGuardCharacteristicsUseCase(
            guardRangeIndex,
            guardValue,
            minGuardOnMap,
            maxGuardOnMap,
            week
        )
        assertThat(value).isEqualTo(Resource.success(GuardCharacteristics(20, 4020, 59, 11859)))
    }

    @Test
    fun `Get GuardCharacteristics, 1-4 import, week correction, returns success`() {

        val guardRangeIndex = 1
        val guardValue = 25000
        val minGuardOnMap = 2
        val maxGuardOnMap = 99
        val week = 3

        val value = getGuardCharacteristicsUseCase(
            guardRangeIndex,
            guardValue,
            minGuardOnMap,
            maxGuardOnMap,
            week
        )
        assertThat(value).isEqualTo(Resource.success(GuardCharacteristics(2, 50000, 5, 125000)))
    }

}