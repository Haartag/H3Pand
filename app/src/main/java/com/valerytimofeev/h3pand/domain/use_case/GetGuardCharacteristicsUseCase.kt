package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.utils.GuardCharacteristics
import com.valerytimofeev.h3pand.utils.Resource

/**
 * Get possible number of guards (min and max) and medium values of guard (min and max).
 * Calculation of the box content is performed based on the average value.
 */
class GetGuardCharacteristicsUseCase {
    operator fun invoke(
        weekCorrectedMin: Double,
        weekCorrectedMax: Double,
        minGuardOnMap: Int,
        maxGuardOnMap: Int,
    ): Resource<GuardCharacteristics> {

        val minRoll = weekCorrectedMin.toInt()
        val maxRoll = weekCorrectedMax.toInt()

        /**
         * Check that the roll is within unit/map limits
         */
        if ((maxRoll + maxRoll.calculateDelta(false)) < minGuardOnMap) return Resource.error(
            "Too weak unit",
            null
        )
        if ((minRoll - minRoll.calculateDelta(true)) > maxGuardOnMap) return Resource.error(
            "Too strong unit",
            null
        )

        val minAverageGuard = minRoll.getMinRoll(minGuardOnMap, maxGuardOnMap)
        val maxAverageGuard = maxRoll.getMaxRoll(minGuardOnMap, maxGuardOnMap)

        val minGuard = minAverageGuard - minAverageGuard.calculateDelta(true)
        val maxGuard = maxAverageGuard + maxAverageGuard.calculateDelta(false)

        return Resource.success(
            GuardCharacteristics(
                minAverage = minAverageGuard,
                minTotal = minGuard,
                maxAverage = maxAverageGuard,
                maxTotal = maxGuard
            )
        )
    }

    /**
     * Calculate min or max difference.
     */
    private fun Int.calculateDelta(
        negative: Boolean
    ): Int {
        return if (negative) {
            ((this * 0.8) * 0.25).toInt()
        } else {
            ((this * 1.33) * 0.25).toInt()
        }
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
        return when {
            this < minGuardOnMap -> {
                minGuardOnMap
            }
            this > maxGuardOnMap -> {
                maxGuardOnMap - maxGuardOnMap.calculateDelta(true)
            }
            else -> {
                this - this.calculateDelta(true)
            }
        }
    }

    /**
     * Calculate possible max. roll of guard.
     * - If calculated number of units is less than minimum possible number of units,
     * the number of units increases.
     *  - If calculated value is greater than the maximum possible
     * number of units, the value is equal to the maximum value.
     * - If calculated number of units is ok, then ok.
     */
    private fun Int.getMaxRoll(
        minGuardOnMap: Int,
        maxGuardOnMap: Int
    ): Int {

        return when {
            this < minGuardOnMap -> {
                minGuardOnMap + minGuardOnMap.calculateDelta(false)
            }
            this > maxGuardOnMap -> {
                maxGuardOnMap
            }
            else -> {
                this + this.calculateDelta(false)
            }
        }
    }
}