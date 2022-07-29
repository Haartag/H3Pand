package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.utils.Constants.MAX_GENERATED_GUARD
import com.valerytimofeev.h3pand.utils.GuardCharacteristics
import com.valerytimofeev.h3pand.utils.GuardRanges
import com.valerytimofeev.h3pand.utils.Resource
import dagger.Provides
import javax.inject.Inject
import kotlin.math.ceil


/**
 * Get possible number of guards (min and max) and guard value (min and max)
 */
class GetGuardCharacteristicsUseCase @Inject constructor(

) {
    operator fun invoke(
        guardRangeIndex: Int,
        guardValue: Int,
        minGuardOnMap: Int,
        maxGuardOnMap: Int,
        week: Int = 1
    ): Resource<GuardCharacteristics> {

        val uncorrectedGuardRange = GuardRanges.range[guardRangeIndex]
            ?: return Resource.error("An unknown error occurred", null)

        val weekCorrectedMin = uncorrectedGuardRange.first.weekCorrection(week)
        val weekCorrectedMax = uncorrectedGuardRange.last.weekCorrection(week)

        val guardRange = weekCorrectedMin..weekCorrectedMax
        //val guardRange = uncorrectedGuardRange

        val minRoll = when (guardRange.first) {
            in 0..2 -> 2
            3 -> 3
            else -> guardRange.first.getMinRoll(minGuardOnMap, maxGuardOnMap)
        }

        val maxRoll = when (guardRange.last) {
            in 0..2 -> 2
            3 -> 3
            else -> guardRange.last.getMaxRoll(maxGuardOnMap)
        }

        if (minRoll > maxGuardOnMap) return Resource.error("Too strong unit", null)
        if (maxRoll < minGuardOnMap) return Resource.error("Too weak unit", null)

        val minGuardValue = minRoll * guardValue
        val maxGuardValue = maxRoll * guardValue

        return Resource.success(
            GuardCharacteristics(
                minRoll,
                minGuardValue,
                maxRoll,
                maxGuardValue
            )
        )
    }

    private fun Int.getMinRoll(
        minGuardOnMap: Int,
        maxGuardOnMap: Int
    ): Int {
        var minRoll = this
        if (ceil(minRoll * 0.8).toInt() < minGuardOnMap) minRoll = minGuardOnMap
        if (minRoll > maxGuardOnMap) minRoll = maxGuardOnMap
        if (ceil(minRoll * 0.8).toInt() > minGuardOnMap) minRoll = ceil(minRoll * 0.8).toInt()
        return minRoll
    }

    private fun Int.getMaxRoll(
        maxGuardOnMap: Int
    ): Int {
        var maxRoll = this
        if ((maxRoll * 1.33).toInt() > maxGuardOnMap) maxRoll = maxGuardOnMap
        if ((maxRoll * 1.33).toInt() < maxGuardOnMap) maxRoll = (maxRoll * 1.33).toInt()
        return maxRoll
    }

    private fun Int.weekCorrection(
        week: Int
    ): Int {
        var weekModifier = 1.0
        repeat(week - 1) {
            weekModifier *= 1.1
        }
        return ceil(this / weekModifier).toInt()
    }
}