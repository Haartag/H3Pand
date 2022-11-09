package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.data.local.BoxValueItem
import com.valerytimofeev.h3pand.data.local.UnitBox
import com.valerytimofeev.h3pand.repositories.local.PandRepository
import com.valerytimofeev.h3pand.domain.model.Difficult
import com.valerytimofeev.h3pand.domain.model.GuardCharacteristics
import com.valerytimofeev.h3pand.utils.Resource
import com.valerytimofeev.h3pand.utils.Status
import javax.inject.Inject
import kotlin.math.roundToInt


/**
 * Get possible boxes and their values for guard value range.
 * @see [GetGuardCharacteristicsUseCase]
 */
class GetBoxForGuardRangeUseCase @Inject constructor(
    private val repository: PandRepository
) {
    suspend operator fun invoke(
        guardRange: GuardCharacteristics,
        difficult: Difficult,
        guardValue: Int,
        additionalValue: Int,
        castleZones: Int,
        zones: Int,
        castle: Int
    ): Resource<List<BoxValueItem>> {

        val minBoxRoll = (guardRange.minAverage * guardValue).valueOfBox(difficult) - additionalValue
        val maxBoxRoll = (guardRange.maxAverage * guardValue).valueOfBox(difficult) - additionalValue

        if (castleZones > zones) return Resource.error("Error: Wrong map settings", null)

        /**
         * Coefficient for AI value cost of unit boxes. Depends on number of zones.
         */
        val unitCoefficient = ((1.0 + (castleZones / zones.toDouble())) * 10).roundToInt() / 10.0

        /**
         * Boxes without units: gold, spells, exp.
         */
        val nonUnitBoxes = repository.getNonUnitBoxesInRange(minBoxRoll, maxBoxRoll)
        /**
         * Boxes with units
         */
        val unitBoxes = repository.getUnitBoxesInRange(
            (minBoxRoll / unitCoefficient).roundToInt(),
            (maxBoxRoll / unitCoefficient).roundToInt(),
            castle
        )

        val allBoxesList = mutableListOf<BoxValueItem>()

        when {
            nonUnitBoxes.status == Status.ERROR && unitBoxes.status == Status.ERROR -> {
                return Resource.error(nonUnitBoxes.message ?: "An unknown error occurred", null)
            }
            nonUnitBoxes.status == Status.ERROR -> {
                allBoxesList.addAll(unitBoxes.data?.map { it.toBoxValueItem(unitCoefficient) }
                    ?: return Resource.error("An unknown error occurred", null))
            }
            unitBoxes.status == Status.ERROR -> {
                allBoxesList.addAll(nonUnitBoxes.data
                    ?: return Resource.error("An unknown error occurred", null))
            }
            else -> {
                allBoxesList.addAll(nonUnitBoxes.data
                    ?: return Resource.error("An unknown error occurred", null))
                allBoxesList.addAll(unitBoxes.data?.map { it.toBoxValueItem(unitCoefficient) }
                    ?: return Resource.error("An unknown error occurred", null)
                )
            }
        }
        return Resource.success(allBoxesList)
    }

    /**
     * Calculate value of box.
     * @param difficult is 1 of 5 sets of constants from [Difficult]
     */
    private fun Int.valueOfBox(
        difficult: Difficult
    ): Int {
        val boxValue = when {
            this <= 2000 -> {
                0
            }
            this <= difficult.minValue1 -> {
                0
            }
            this <= difficult.minValue2 -> {
                ((this / difficult.coefficient1) + difficult.minValue1).toInt()
            }
            else -> {
                ((this + (difficult.minValue1 * difficult.coefficient1) + (difficult.minValue2 * difficult.coefficient2)) / (difficult.coefficient1 + difficult.coefficient2)).toInt()
            }
        }
        return boxValue
    }

    /**
     * Make fake BoxValueItem from UnitBox
     */
    private fun UnitBox.toBoxValueItem(unitCoefficient: Double): BoxValueItem {
        return BoxValueItem(
            0,
            "${this.name} ${this.numberInBox}",
            "${this.nameRu} ${this.numberInBox}",
            ((this.AIValue * this.numberInBox) * unitCoefficient).roundToInt(),
            img,
            "Unit"
        )
    }
}