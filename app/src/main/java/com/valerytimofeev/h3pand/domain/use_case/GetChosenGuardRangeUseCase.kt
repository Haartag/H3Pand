package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.data.local.additional_data.GuardRanges

class GetChosenGuardRangeUseCase {
    operator fun invoke(index: Int): IntRange {
        return GuardRanges.range.getOrDefault(index, -1..-1)
    }

    operator fun invoke(range: IntRange): IntRange {
        return range
    }
}