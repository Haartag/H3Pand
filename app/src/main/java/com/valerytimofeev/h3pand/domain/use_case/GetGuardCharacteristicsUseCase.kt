package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.utils.GuardCharacteristics
import com.valerytimofeev.h3pand.utils.GuardRanges
import com.valerytimofeev.h3pand.utils.Resource
import kotlin.math.ceil


/**
 * Get possible number of guards (min and max) and guard value (min and max)
 */
class GetGuardCharacteristicsUseCase() {
    operator fun invoke(
        guardRangeIndex: Int,
        guardValue: Int,
        valueRange: IntRange,
        minGuardOnMap: Int,
        needCorrection: Boolean = false
    ): Resource<GuardCharacteristics> {

        val guardRange = GuardRanges.range[guardRangeIndex]
            ?: return Resource.error("An unknown error occurred", null)

        var minRoll = when (guardRange.first) {
            in 0..2 -> 2
            3 -> 3
            else -> guardRange.first
        }
        if (minRoll < minGuardOnMap) minRoll = minGuardOnMap

        var maxRoll = when {
            guardRange.last >= 100 -> return Resource.error("Too much guards", null)
            else -> guardRange.last
        }
        if (maxRoll < minGuardOnMap) return Resource.error("Too weak unit", null)

        val minGuardValue = when {
            minRoll * guardValue > valueRange.last -> {
                return Resource.error("Too strong unit", null)
            }
            minRoll * guardValue >= valueRange.first -> minRoll * guardValue
            else -> {
                minRoll = ceil(valueRange.first / guardValue.toDouble()).toInt()
                minRoll * guardValue
            }
        }

        val maxGuardValue = when {
            maxRoll * guardValue < valueRange.first -> {
                return Resource.error("Too weak unit", null)
            }
            maxRoll * guardValue <= valueRange.last -> maxRoll * guardValue
            else -> {
                maxRoll = (valueRange.last / guardValue.toDouble()).toInt()
                maxRoll * guardValue
            }
        }

        return if (needCorrection) {
            Resource.success(
                GuardCharacteristics(
                    minRoll,
                    minGuardValue,
                    maxRoll,
                    maxGuardValue
                ).correct(guardValue)
            )
        } else {
            Resource.success(GuardCharacteristics(minRoll, minGuardValue, maxRoll, maxGuardValue))
        }
    }

    /**
     * As box have 25% spread of guard, guard range must be corrected.
     */
    private fun GuardCharacteristics.correct(
        guardValue: Int
    ): GuardCharacteristics {

        val minRoll = when (this.minRoll) {
            in 0..3 -> this.minRoll
            else -> ceil(this.minRoll * 0.75).toInt()
        }
        val maxRoll = when (this.maxRoll) {
            in 0..3 -> this.maxRoll
            else -> (this.maxRoll * 1.25).toInt()
        }
        val minValue = minRoll * guardValue
        val maxValue = maxRoll * guardValue

        return GuardCharacteristics(minRoll, minValue, maxRoll, maxValue)
    }
}