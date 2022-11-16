package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.data.local.additional_data.CastleSettings
import com.valerytimofeev.h3pand.data.local.database.BoxValueItem
import com.valerytimofeev.h3pand.data.local.database.UnitBox
import com.valerytimofeev.h3pand.repositories.local.PandRepository
import com.valerytimofeev.h3pand.utils.Resource
import com.valerytimofeev.h3pand.utils.Status
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * Get all boxes: from the database, as well as converted from units
 */
class GetAllAvailableBoxesUseCase @Inject constructor(
    private val repository: PandRepository
) {
    suspend operator fun invoke(
        castle: Int,
        castleZones: Int,
        zones: Int,
    ): Resource<List<BoxValueItem>> {

        if (castle > CastleSettings.values().size) return Resource.error("An unknown error occurred", null)
        if (castleZones > zones) return Resource.error("An unknown error occurred", null)
        /**
         * Boxes without units: gold, spells, exp.
         */
        val nonUnitBoxes = repository.getAllNonUnitBoxes()

        /**
         * Coefficient for AI value cost of unit boxes. Depends on number of zones.
         */
        val unitCoefficient = ((1.0 + (castleZones / zones.toDouble())) * 10).roundToInt() / 10.0

        /**
         * Boxes with units
         */
        val unitBoxes = repository.getAllUnitBoxesByCastle(castle)

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
                allBoxesList.addAll(
                    nonUnitBoxes.data
                        ?: return Resource.error("An unknown error occurred", null)
                )
            }
            else -> {
                allBoxesList.addAll(
                    nonUnitBoxes.data
                        ?: return Resource.error("An unknown error occurred", null)
                )
                allBoxesList.addAll(unitBoxes.data?.map { it.toBoxValueItem(unitCoefficient) }
                    ?: return Resource.error("An unknown error occurred", null)
                )
            }
        }
        return Resource.success(allBoxesList)
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