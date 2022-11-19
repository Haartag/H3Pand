package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.data.local.additional_data.MapSettings
import com.valerytimofeev.h3pand.data.local.additional_data.TextWithLocalization
import com.valerytimofeev.h3pand.domain.model.BoxWithDropChance
import com.valerytimofeev.h3pand.domain.model.BoxWithGuard
import com.valerytimofeev.h3pand.domain.model.GuardCharacteristics
import com.valerytimofeev.h3pand.utils.Resource
import com.valerytimofeev.h3pand.utils.Status
import javax.inject.Inject
import kotlin.math.pow

/**
 * Get drop chances for valid box.
 */
class GetBoxWithPercentUseCase @Inject constructor(
    private val getUnitDropCoefficientUseCase: GetUnitDropCoefficientUseCase
) {
    suspend operator fun invoke(
        boxWithGuard: BoxWithGuard,
        guardValue: GuardCharacteristics,
        week: Int,
        chosenGuardRange: IntRange,
        castle: Int,
        numberOfZones: Float,
        numberOfUnitZones: Float,
        mapSettings: MapSettings,
        zoneType: Int,
        exactlyGuard: Boolean
    ): Resource<BoxWithDropChance> {

        val numbersList = (boxWithGuard.guardNumber.minGuard
                ..boxWithGuard.guardNumber.maxGuard).toList()

        val unitDropCoefficientResource = getUnitDropCoefficientUseCase(
            boxWithGuard.box,
            castle,
            numberOfZones,
            numberOfUnitZones,
            mapSettings,
            zoneType,
            exactlyGuard
        )
        if (unitDropCoefficientResource.status == Status.ERROR) {
            return Resource.error(
                unitDropCoefficientResource.message
                    ?: "An unknown error occurred", null
            )
        }

        val numbersWithPercentsMap = numbersList.getPercents(
            (unitDropCoefficientResource.data!! * 10000).toInt()
        )

        val guards = mutableListOf<Int>()
        var summaryPercent = 0.0
        var mostLikelyGuardNumber = 0 to 0.0


        //Adding all valid boxes to list and summing up their percentages
        run breaking@{
            numbersWithPercentsMap.forEach {
                if (
                    it.key in guardValue.minTotal..guardValue.maxTotal &&
                    it.key.weekCorrectionUndo(week) in chosenGuardRange
                ) {
                    summaryPercent += it.value
                    guards.add(it.key.weekCorrectionUndo(week))
                    if (it.value > mostLikelyGuardNumber.second) {
                        mostLikelyGuardNumber = it.key.weekCorrectionUndo(week) to it.value
                    }
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
            BoxWithDropChance(
                TextWithLocalization(boxWithGuard.box.boxContent, boxWithGuard.box.boxContentRu),
                summaryPercent,
                mostLikelyGuardNumber.first,
                if (exactlyGuard) {
                    boxWithGuard.guardNumber.minGuard.weekCorrectionUndo(week)..
                            boxWithGuard.guardNumber.maxGuard.weekCorrectionUndo(week)
                } else {
                    guards.first()..guards.last()
                       },
                boxWithGuard.box.type,
                boxWithGuard.box.img
            )
        )
    }

    /**
     * Calculate drop chance. Each 2 additional values gives +1 to previous item chance
     * and + item count to divider.
     *  - ex. 1/1 -> 1/4 - 2/4 - 1/4 -> 1/9 - 2/9 - 3/9 - 2/9 - 1/9 e.t.c.
     */
    private fun List<Int>.getPercents(multiplier: Int): Map<Int, Double> {
        var divider = 1

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

    /**
     * Reverse weekly increase of guard to the value of first week
     */
    private fun Int.weekCorrectionUndo(
        week: Int
    ): Int {
        return (this * 1.1.pow(week - 1)).toInt()
    }
}
