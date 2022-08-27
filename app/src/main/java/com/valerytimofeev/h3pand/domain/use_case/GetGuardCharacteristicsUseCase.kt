package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.utils.GuardCharacteristics
import com.valerytimofeev.h3pand.utils.Resource
import com.valerytimofeev.h3pand.utils.ceilToInt
import javax.inject.Inject
import kotlin.math.ceil


/**
 * Get possible number of guards (min and max) and guard value (min and max)
 */
class GetGuardCharacteristicsUseCase @Inject constructor(

) {
    operator fun invoke(
        weekCorrectedMin: Double,
        weekCorrectedMax: Double,
        guardValue: Int,
        minGuardOnMap: Int,
        maxGuardOnMap: Int,
    ): Resource<GuardCharacteristics> {

        /**
         * It can't be less than 2 guards
         */
        val minRoll = when (weekCorrectedMin.ceilToInt()) {
            in 0..2 -> 2
            3 -> 3
            else -> weekCorrectedMin.ceilToInt().getMinRoll(minGuardOnMap, maxGuardOnMap)
        }

        val maxRoll = when (weekCorrectedMax.ceilToInt()) {
            in 0..2 -> 2
            3 -> 3
            else -> weekCorrectedMax.ceilToInt().getMaxRoll(maxGuardOnMap)
        }

        /**
         * Check that the roll is within unit limits
         */
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

    /**
     * Calculate possible min. roll of guard.
     *  - If calculated number of units is less than minimum possible number of units,
     * the number of units increases.
     *  - If calculated value is greater than the maximum possible
     * number of units, the value is equal to the maximum value.
     *  - If calculated number of units is ok, then ok.
     */
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

    /**
     * Calculate possible max. roll of guard.
     *  - If calculated value is greater than the maximum possible
     * number of units, the value is equal to the maximum value.
     * - If calculated number of units is ok, then ok.
     */
    private fun Int.getMaxRoll(
        maxGuardOnMap: Int
    ): Int {
        var maxRoll = this
        if ((maxRoll * 1.33).toInt() > maxGuardOnMap) maxRoll = maxGuardOnMap
        if ((maxRoll * 1.33).toInt() < maxGuardOnMap) maxRoll = (maxRoll * 1.33).toInt()
        return maxRoll
    }
}