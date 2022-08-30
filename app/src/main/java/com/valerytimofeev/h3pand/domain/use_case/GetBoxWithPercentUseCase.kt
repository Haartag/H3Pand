package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.data.local.BoxValueItem
import com.valerytimofeev.h3pand.utils.*
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * Get drop chances for valid box.
 * Box obtained from GetBoxForGuardRangeUseCase
 */
class GetBoxWithPercentUseCase @Inject constructor(

) {
    operator fun invoke(
        boxValueItem: BoxValueItem,
        guardValue: GuardCharacteristics,
        difficult: Difficult,
        unitValue: Int,
        week: Int,
        chosenGuardRange: IntRange,
        additionalValue: Int
    ): Resource<BoxWithDropPercent> {

        val sumValue = boxValueItem.value + additionalValue

        val midGuardValue =
            if ((sumValue - difficult.minValue2) * difficult.coefficient2 > 0) {
                ((sumValue - difficult.minValue1) * difficult.coefficient1 + (sumValue - difficult.minValue2) * difficult.coefficient2).roundToInt()
            } else {
                ((sumValue - difficult.minValue1) * difficult.coefficient1).roundToInt()
            }

        val midGuardNumber = (midGuardValue / unitValue.toDouble()).roundToInt()
        val randomWindowSize = (midGuardNumber * 0.25).toInt()

        val lowGuardNumber = midGuardNumber - randomWindowSize
        val hiGuardNumber = midGuardNumber + randomWindowSize

        val numbersList = (lowGuardNumber..hiGuardNumber).map { it }

        val numbersWithPercentsMap = numbersList.getPercents(
            if (boxValueItem.boxContent.contains("exp.")) 2 else 1
        )

        val guards = mutableListOf<Int>()
        var summaryPercent = 0.0

        /**
         * Adding all valid boxes to list and summing up their percentages
         */
        run breaking@{
            numbersWithPercentsMap.forEach {
                if (
                    it.key in guardValue.minRoll..guardValue.maxRoll && it.key.weekCorrectionUndo(
                        week
                    ) in chosenGuardRange
                ) {
                    summaryPercent += it.value
                    guards.add(it.key.weekCorrectionUndo(week))
                    /**
                     * For some week values, the calculated upper range value will be less than
                     * the upper guardRange value (for example, 48 instead of 49, etc.).
                     * This is because some values are not possible when calculating
                     * the week-adjusted value. But the user expects the upper range
                     * (when going beyond the guardRange) will be equal to the upper range
                     * of the guardRange, not another number.
                     *
                     * Therefore, a fake value equal to the upper limit of guardRange is added to
                     * guards, with zero drop percent.
                     * Tests that take this trick into account are marked with !!.
                     */
                } else if (it.key.weekCorrectionUndo(week) > chosenGuardRange.last && guards.isNotEmpty()) {
                    guards.add(chosenGuardRange.last)
                    return@breaking
                }
            }
        }

        if (guards.isEmpty()) return Resource.error("An unknown error occurred", null)

        return Resource.success(
            BoxWithDropPercent(
                boxValueItem.boxContent,
                summaryPercent,
                guards.first()..guards.last(),
                boxValueItem.img
            )
        )
    }

    private fun List<Int>.getPercents(multiplier: Int): Map<Int, Double> {

        var divider = 1

        /**
         * Calculate drop chance. Each 2 additional values gives +1 to previous item chance
         * and + item count to divider.
         * ex. 1/1 -> 1/4 - 2/4 - 1/4 -> 1/9 - 2/9 - 3/9 - 2/9 - 1/9 e.t.c.
         */
        for (index in 1 until this.size step 2) {
            divider += (2 + index)
        }

        val percentList = mutableListOf<Double>()

        for (number in 1..this.size / 2 + 1) {
            percentList.add(((number.toDouble() / divider) * 100) * multiplier)
        }
        for (number in this.size / 2 downTo 1) {
            percentList.add(((number.toDouble() / divider) * 100) * multiplier)
        }

        return this.zip(percentList).toMap()
    }

    private fun Int.weekCorrectionUndo(
        week: Int
    ): Int {
        var weekModifier = 1.0
        repeat(week - 1) {
            weekModifier *= 1.1
        }
        return (this * weekModifier).ceilToInt()
    }
}
