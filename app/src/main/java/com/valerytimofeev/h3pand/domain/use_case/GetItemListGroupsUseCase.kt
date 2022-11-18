package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.domain.model.BoxWithDropChance
import com.valerytimofeev.h3pand.utils.Constants.MIN_PERCENT

/**
 * Group the boxes by their type, save content for tile in [ListGroup] data class.
 */
class GetItemListGroupsUseCase {
    operator fun invoke(
        boxList: List<BoxWithDropChance>
    ): List<ListGroup> {
        val groupTypes = listOf("Exp", "Gold", "Spell", "Unit")
        return groupTypes.mapNotNull { makeListGroup(it, boxList) }
    }

    data class ListGroup(
        val type: String,
        val img: String,
        val summaryPercent: Double,
        val content: List<BoxWithDropChance>
    )

    private fun makeListGroup(
        type: String,
        boxList: List<BoxWithDropChance>
    ): ListGroup? {
        val expItems = boxList.filter { it.type == type && it.dropChance >= MIN_PERCENT}
        if (expItems.isEmpty()) return null
        return ListGroup(
            type = expItems[0].type,
            img = expItems.sortedBy { it.mostLikelyGuard }.last().img, //maximum reward image
            summaryPercent = expItems.sumOf { it.dropChance },
            content = expItems
        )
    }
}