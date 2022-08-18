package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.data.local.BoxValueItem
import com.valerytimofeev.h3pand.data.local.UnitBox
import com.valerytimofeev.h3pand.repositories.local.PandRepository
import com.valerytimofeev.h3pand.utils.Difficult
import com.valerytimofeev.h3pand.utils.GuardCharacteristics
import com.valerytimofeev.h3pand.utils.Resource
import com.valerytimofeev.h3pand.utils.Status
import javax.inject.Inject
import kotlin.math.roundToInt


/**
 * Get possible boxes and their values for guard value range
 */
class GetBoxForGuardRangeUseCase @Inject constructor(
    private val repository: PandRepository
) {
    suspend operator fun invoke(
        guardRange: GuardCharacteristics,
        difficult: Difficult,
        additionalValue: Int,
        castleZones: Int,
        zones: Int,
        castle: Int
    ): Resource<List<BoxValueItem>> {

        val minBoxRoll = (guardRange.minValue).valueOfBox(difficult) - additionalValue
        val maxBoxRoll = (guardRange.maxValue).valueOfBox(difficult) - additionalValue

        val nonUnitBoxes = repository.getNonUnitBoxesInRange(minBoxRoll, maxBoxRoll)

        val unitCoefficient = ((1.0 + (castleZones / zones.toDouble())) * 10).roundToInt() / 10.0

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
     * Calculate value of box from guard
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

    private fun UnitBox.toBoxValueItem(unitCoefficient: Double): BoxValueItem {
        return BoxValueItem(
            0,
            "${this.name} ${this.numberInBox}",
            ((this.AIValue * this.numberInBox) * unitCoefficient).roundToInt(),
            "img"
        )
    }

}