package com.valerytimofeev.h3pand.domain.use_case

import com.google.common.truth.Truth
import com.valerytimofeev.h3pand.data.additional_data.ValueRange
import com.valerytimofeev.h3pand.utils.Resource
import org.junit.Before
import org.junit.Test


class GetZoneValueRangeUseCaseTest {
    private lateinit var getZoneValueRangeUseCase: GetZoneValueRangeUseCase

    @Before
    fun setup() {
        getZoneValueRangeUseCase = GetZoneValueRangeUseCase()
    }

    @Test
    fun `Get zone value range, valid input, returns success`() {
        val ranges = listOf(
            ValueRange(0, 300..3000, 14),
            ValueRange(1, 5000..16000, 6),
            ValueRange(2, 12000..22000, 1)
        )
        val result = getZoneValueRangeUseCase(ranges)

        Truth.assertThat(result.data).isEqualTo(300..22000)
    }

    @Test
    fun `Get zone value range, one item input, returns success`() {
        val ranges = listOf(
            ValueRange(0, 300..3000, 14),
        )
        val result = getZoneValueRangeUseCase(ranges)

        Truth.assertThat(result.data).isEqualTo(300..3000)
    }

    @Test
    fun `Get zone value range, empty input, returns success`() {
        val ranges = listOf<ValueRange>()
        val result = getZoneValueRangeUseCase(ranges)

        Truth.assertThat(result).isEqualTo(Resource.error("An unknown error occurred", null))
    }

    @Test
    fun `Get zone value range, min greater max input, returns success`() {
        val ranges = listOf<ValueRange>(
            ValueRange(1, 25000..16000, 6),
            ValueRange(2, 25000..22000, 1)
        )
        val result = getZoneValueRangeUseCase(ranges)

        Truth.assertThat(result).isEqualTo(Resource.error("Error: wrong map settings", null))
    }

    @Test
    fun `Get zone value range, negative min input, returns success`() {
        val ranges = listOf<ValueRange>(
            ValueRange(1, -100..16000, 6),
            ValueRange(2, -1000..22000, 1)
        )
        val result = getZoneValueRangeUseCase(ranges)

        Truth.assertThat(result).isEqualTo(Resource.error("Error: wrong map settings", null))
    }

    @Test
    fun `Get zone value range, negative min and max input, returns success`() {
        val ranges = listOf<ValueRange>(
            ValueRange(1, -100..0, 6),
            ValueRange(2, -1000..-500, 1)
        )
        val result = getZoneValueRangeUseCase(ranges)

        Truth.assertThat(result).isEqualTo(Resource.error("Error: wrong map settings", null))
    }
}