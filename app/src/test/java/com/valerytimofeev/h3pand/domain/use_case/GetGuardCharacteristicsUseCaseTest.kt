package com.valerytimofeev.h3pand.domain.use_case

import com.google.common.truth.Truth.assertThat
import com.valerytimofeev.h3pand.domain.model.GuardCharacteristics
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
        val minGuardOnMap = 20
        val maxGuardOnMap = 99

        val value = getGuardCharacteristicsUseCase(
            weekCorrectedMin,
            weekCorrectedMax,
            minGuardOnMap,
            maxGuardOnMap,
        )
        assertThat(value).isEqualTo(Resource.success(GuardCharacteristics(16, 13, 65, 86)))
    }

    @Test
    fun `Get GuardCharacteristics, 1-4 unit, returns success`() {

        val weekCorrectedMin = 1.0
        val weekCorrectedMax = 4.0
        val minGuardOnMap = 2
        val maxGuardOnMap = 99

        val value = getGuardCharacteristicsUseCase(
            weekCorrectedMin,
            weekCorrectedMax,
            minGuardOnMap,
            maxGuardOnMap,
        )
        assertThat(value).isEqualTo(Resource.success(GuardCharacteristics(2, 2, 5, 6)))
    }

    @Test
    fun `Get GuardCharacteristics, range under min guards, returns success`() {

        val weekCorrectedMin = 10.0
        val weekCorrectedMax = 19.0
        val minGuardOnMap = 20
        val maxGuardOnMap = 99

        val value = getGuardCharacteristicsUseCase(
            weekCorrectedMin,
            weekCorrectedMax,
            minGuardOnMap,
            maxGuardOnMap,
        )
        assertThat(value).isEqualTo(Resource.success(GuardCharacteristics(20, 16, 26, 34)))
    }

    @Test
    fun `Get GuardCharacteristics, range above max guards, returns success`() {

        val weekCorrectedMin = 100.0
        val weekCorrectedMax = 249.0
        val minGuardOnMap = 20
        val maxGuardOnMap = 99

        val value = getGuardCharacteristicsUseCase(
            weekCorrectedMin,
            weekCorrectedMax,
            minGuardOnMap,
            maxGuardOnMap,
        )
        assertThat(value).isEqualTo(Resource.success(GuardCharacteristics(80, 64, 99, 131)))
    }

    @Test
    fun `Get GuardCharacteristics, all units below min threshold, returns error`() {

        val weekCorrectedMin = 5.0
        val weekCorrectedMax = 9.0
        val minGuardOnMap = 18
        val maxGuardOnMap = 99

        val value = getGuardCharacteristicsUseCase(
            weekCorrectedMin,
            weekCorrectedMax,
            minGuardOnMap,
            maxGuardOnMap,
        )
        assertThat(value).isEqualTo(Resource.error("Too weak unit", null))
    }

    @Test
    fun `Get GuardCharacteristics, all units above max threshold (fake min), returns error`() {

        val weekCorrectedMin = 200.0
        val weekCorrectedMax = 249.0
        val minGuardOnMap = 10
        val maxGuardOnMap = 99

        val value = getGuardCharacteristicsUseCase(
            weekCorrectedMin,
            weekCorrectedMax,
            minGuardOnMap,
            maxGuardOnMap,
        )
        assertThat(value).isEqualTo(Resource.error("Too strong unit", null))
    }


    @Test
    fun `Get GuardCharacteristics, valid import, week corrected guard range, returns success`() {

        val weekCorrectedMin = 20.0 * 1.1
        val weekCorrectedMax = 49.0 * 1.1
        val minGuardOnMap = 20
        val maxGuardOnMap = 99

        val value = getGuardCharacteristicsUseCase(
            weekCorrectedMin,
            weekCorrectedMax,
            minGuardOnMap,
            maxGuardOnMap,
        )
        assertThat(value).isEqualTo(Resource.success(GuardCharacteristics(18, 15, 70, 93)))
    }
}