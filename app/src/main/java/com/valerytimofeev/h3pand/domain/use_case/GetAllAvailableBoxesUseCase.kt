package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.data.local.additional_data.CastleSettings
import com.valerytimofeev.h3pand.data.local.additional_data.MapSettings
import com.valerytimofeev.h3pand.data.local.database.BoxValueItem
import com.valerytimofeev.h3pand.data.local.database.UnitBox
import com.valerytimofeev.h3pand.domain.use_case.calculation_pand_use_case.RemoveUnnecessaryBoxesUseCase
import com.valerytimofeev.h3pand.repositories.local.PandRepository
import com.valerytimofeev.h3pand.utils.Resource
import com.valerytimofeev.h3pand.utils.Status
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * Get all boxes: from the database, as well as converted from units
 */
class GetAllAvailableBoxesUseCase @Inject constructor(
    private val repository: PandRepository,
    private val removeUnnecessaryBoxesUseCase: RemoveUnnecessaryBoxesUseCase,
) {
    suspend operator fun invoke(
        mapSettings: MapSettings,
        currentZone: Int,
        castle: Int,
        castleZones: Int,
        zones: Int,
    ): Resource<List<BoxValueItem>> {

        if (castle > CastleSettings.values().size) return Resource.error(
            "An unknown error occurred",
            null
        )
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
        val boxWithRestrictionsList = allBoxesList.removeRestricted(mapSettings, currentZone)
        if (boxWithRestrictionsList.isEmpty()) return Resource.error("Error: Wrong map settings", null)
        return Resource.success(boxWithRestrictionsList)
    }

    /**
     * Make fake BoxValueItem from UnitBox
     */
    private fun UnitBox.toBoxValueItem(unitCoefficient: Double): BoxValueItem {
        return BoxValueItem(
            1000 + this.id, //Id of creature box is 1000 + id of creature in this box
            "${this.name} ${this.numberInBox}",
            "${this.nameRu} ${this.numberInBox}",
            ((this.AIValue * this.numberInBox) * unitCoefficient).roundToInt(),
            img,
            "Unit"
        )
    }

    private fun List<BoxValueItem>.removeRestricted(
        mapSettings: MapSettings,
        currentZone: Int
    ): List<BoxValueItem> {

        val zoneRestrictions = mapSettings.valueRanges.getOrNull(currentZone)?.zoneRestrictedBoxes
        if (mapSettings.boxRestrictions == null && zoneRestrictions == null) return this

        val restrictedTypes = mutableListOf<String>()
        restrictedTypes.addAll(mapSettings.boxRestrictions?.restrictedTypes.orEmpty())
        restrictedTypes.addAll(zoneRestrictions?.restrictedTypes.orEmpty())

        val restrictedIds = mutableListOf<Int>()
        restrictedIds.addAll(mapSettings.boxRestrictions?.restrictedIds.orEmpty())
        restrictedIds.addAll(zoneRestrictions?.restrictedIds.orEmpty())

        val newBoxes = mutableListOf<BoxValueItem>()
        newBoxes.addAll(mapSettings.boxRestrictions?.newBoxes.orEmpty())
        newBoxes.addAll(zoneRestrictions?.newBoxes.orEmpty())

        var allBoxes = this

        if (restrictedTypes.isNotEmpty()) {
            with(removeUnnecessaryBoxesUseCase) {
                allBoxes = allBoxes.removeByTypes(restrictedTypes)
            }
        }
        if (restrictedIds.isNotEmpty()) {
            with(removeUnnecessaryBoxesUseCase) {
                allBoxes = allBoxes.removeByIds(restrictedIds)
            }
        }
        if (newBoxes.isNotEmpty()) {
            with(removeUnnecessaryBoxesUseCase) {
                allBoxes = allBoxes.addBoxes(newBoxes)
            }
        }
        return allBoxes
    }
}