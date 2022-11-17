package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.data.local.database.BoxValueItem
import com.valerytimofeev.h3pand.data.local.database.Guard
import com.valerytimofeev.h3pand.data.local.additional_data.Difficult
import com.valerytimofeev.h3pand.domain.model.BoxWithGuard
import com.valerytimofeev.h3pand.domain.model.GuardNumber
import com.valerytimofeev.h3pand.utils.Constants.MAX_GENERATED_GUARD
import com.valerytimofeev.h3pand.utils.ceilToInt
import kotlin.math.roundToInt

/**
 * Get all valid boxes and number of guard (without week correction)
 */
class GetValidBoxesAndGuardsUseCase {
    operator fun invoke(
        boxes: List<BoxValueItem>,
        guard: Guard,
        additionalValue: Int,
        difficult: Difficult,
        valueRange: IntRange,
    ): List<BoxWithGuard> {

        val boxesWithGuards = mutableListOf<BoxWithGuard>()
        val minGuardNumber = ((guard.minOnMap + guard.maxOnMap) / 2.0).toInt()
        boxes.forEach {
            val boxWithGuard = getBoxWithGuard(
                it,
                additionalValue,
                guard.AIValue,
                difficult,
                minGuardNumber
            )
            if (boxWithGuard != null) boxesWithGuards.add(boxWithGuard)
        }
        return boxesWithGuards.filterByBoxValue(valueRange)
    }

    /**
     * If box have valid guard, return box with guard number (min, average and max)
     */
    private fun getBoxWithGuard(
        box: BoxValueItem,
        additionalValue: Int,
        guardValue: Int,
        difficult: Difficult,
        minGuardNumber: Int
    ): BoxWithGuard? {
        val guardSumValue = (box.value + additionalValue).getGuardValue(difficult) ?: return null
        val averageGuard = (guardSumValue.toDouble() / guardValue).roundToInt()
        if (averageGuard !in minGuardNumber..MAX_GENERATED_GUARD) return null
        val delta = (averageGuard * 0.25).toInt()
        val minGuard = maxOf((minGuardNumber * 0.75).ceilToInt(), averageGuard - delta)
        val maxGuard = minOf((MAX_GENERATED_GUARD * 1.25).toInt(), averageGuard + delta)

        return BoxWithGuard(box, GuardNumber(minGuard, averageGuard, maxGuard))
    }

    /**
     * Calculate average guard value from pandora box value and additional items value
     */
    private fun Int.getGuardValue(difficult: Difficult): Int? {
        return when {
            this <= 2000 -> null
            this <= difficult.minValue1 -> null
            this <= difficult.minValue2 -> {
                ((this - difficult.minValue1) * difficult.coefficient1).toInt()
            }
            else -> {
                val firstPart = ((this - difficult.minValue1) * difficult.coefficient1).toInt()
                val secondPart = ((this - difficult.minValue2) * difficult.coefficient2).toInt()
                firstPart + secondPart
            }
        }
    }

    /**
     * Filter boxes, returns only those that meet the value requirements of the current map zone
     */
    private fun List<BoxWithGuard>.filterByBoxValue(valueRange: IntRange): List<BoxWithGuard> {
        return this.filter { it.box.value in valueRange}
    }

}