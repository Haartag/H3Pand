package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.data.local.additional_data.ValueRange
import com.valerytimofeev.h3pand.utils.Constants.MIN_PAND_VALUE
import com.valerytimofeev.h3pand.utils.Resource

class GetZoneValueRangeUseCase {
    operator fun invoke(
        ranges: List<ValueRange>
    ): Resource<IntRange> {
        val min: Int
        val max: Int
        try {
            min = ranges.minOf { it.range.first }
            max = ranges.maxOf { it.range.last }
        } catch (e: Exception) {
            return Resource.error("An unknown error occurred", null)
        }
        if (min > max || max < 0 || min < 0) {
            return Resource.error("Error: wrong map settings", null)
        }

        return Resource.success(minOf(min, MIN_PAND_VALUE)..max)
    }
}