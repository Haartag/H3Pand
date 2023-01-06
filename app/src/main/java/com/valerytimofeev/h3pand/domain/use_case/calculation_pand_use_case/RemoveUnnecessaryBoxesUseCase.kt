package com.valerytimofeev.h3pand.domain.use_case.calculation_pand_use_case

import com.valerytimofeev.h3pand.data.local.database.BoxValueItem

/**
 * Remove the boxes that are prohibited by the map settings
 */
class RemoveUnnecessaryBoxesUseCase {

    /**
     * Remove the boxes by type
     */
    fun List<BoxValueItem>.removeByTypes(
        typeToBeRemoved: List<String>
    ): List<BoxValueItem> {
        return this.filter {
            !typeToBeRemoved.contains(it.type)
        }
    }

    /**
     * Remove the boxes by Id
     */
    fun List<BoxValueItem>.removeByIds(
        idsToBeRemoved: List<Int>
    ): List<BoxValueItem> {
        val unitBoxesToBeRemoved = idsToBeRemoved.filter { it > 1000 }
        val nonUnitBoxesToBeRemoved = idsToBeRemoved.filter { it < 1000 }
        return this
            .filter {!nonUnitBoxesToBeRemoved.contains(it.id)}
            .removeUnitBoxes(unitBoxesToBeRemoved)
    }

    /**
     * Add map-special boxes
     */
    fun List<BoxValueItem>.addBoxes(
        boxes: List<BoxValueItem>
    ): List<BoxValueItem> {
        return this + boxes
    }

    /**
     * Remove the creature boxes by Id
     */
    private fun List<BoxValueItem>.removeUnitBoxes(
        creatureId: List<Int>
    ): List<BoxValueItem> {
        return this.filter {
            !creatureId.contains(it.id)
        }
    }
}