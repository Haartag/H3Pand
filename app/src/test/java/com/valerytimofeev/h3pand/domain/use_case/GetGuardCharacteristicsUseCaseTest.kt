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

        val weekCorrectedMin = 20.0
        val weekCorrectedMax = 49.0
        val guardValue = 201
        val minGuardOnMap = 20
        val maxGuardOnMap = 99

        val value = getGuardCharacteristicsUseCase(
            weekCorrectedMin,
            weekCorrectedMax,
            guardValue,
            minGuardOnMap,
            maxGuardOnMap,
        )
        assertThat(value).isEqualTo(Resource.success(GuardCharacteristics(20, 4020, 65, 13065)))
    }

    @Test
    fun `Get GuardCharacteristics, 1-4 unit, returns success`() {

        val weekCorrectedMin = 1.0
        val weekCorrectedMax = 4.0
        val guardValue = 25000
        val minGuardOnMap = 2
        val maxGuardOnMap = 99

        val value = getGuardCharacteristicsUseCase(
            weekCorrectedMin,
            weekCorrectedMax,
            guardValue,
            minGuardOnMap,
            maxGuardOnMap,
        )
        assertThat(value).isEqualTo(Resource.success(GuardCharacteristics(2, 50000, 5, 125000)))
    }

    @Test
    fun `Get GuardCharacteristics, range under min guards, returns success`() {

        val weekCorrectedMin = 10.0
        val weekCorrectedMax = 19.0
        val guardValue = 201
        val minGuardOnMap = 20
        val maxGuardOnMap = 99

        val value = getGuardCharacteristicsUseCase(
            weekCorrectedMin,
            weekCorrectedMax,
            guardValue,
            minGuardOnMap,
            maxGuardOnMap,
        )
        assertThat(value).isEqualTo(Resource.success(GuardCharacteristics(20, 4020, 25, 5025)))
    }

    @Test
    fun `Get GuardCharacteristics, range above max guards, returns success`() {

        val weekCorrectedMin = 100.0
        val weekCorrectedMax = 249.0
        val guardValue = 201
        val minGuardOnMap = 20
        val maxGuardOnMap = 99

        val value = getGuardCharacteristicsUseCase(
            weekCorrectedMin,
            weekCorrectedMax,
            guardValue,
            minGuardOnMap,
            maxGuardOnMap,
        )
        assertThat(value).isEqualTo(Resource.success(GuardCharacteristics(80, 16080, 99, 19899)))
    }

    @Test
    fun `Get GuardCharacteristics, all units below min threshold, returns error`() {

        val weekCorrectedMin = 5.0
        val weekCorrectedMax = 9.0
        val guardValue = 270
        val minGuardOnMap = 18
        val maxGuardOnMap = 99

        val value = getGuardCharacteristicsUseCase(
            weekCorrectedMin,
            weekCorrectedMax,
            guardValue,
            minGuardOnMap,
            maxGuardOnMap,
        )
        assertThat(value).isEqualTo(Resource.error("Too weak unit", null))
    }

    @Test
    fun `Get GuardCharacteristics, valid import, week corrected guard range, returns success`() {

        val weekCorrectedMin = 20.0 * 1.1
        val weekCorrectedMax = 49.0 * 1.1
        val guardValue = 201
        val minGuardOnMap = 20
        val maxGuardOnMap = 99

        val value = getGuardCharacteristicsUseCase(
            weekCorrectedMin,
            weekCorrectedMax,
            guardValue,
            minGuardOnMap,
            maxGuardOnMap,
        )
        assertThat(value).isEqualTo(Resource.success(GuardCharacteristics(20, 4020, 71, 14271)))
    }
}