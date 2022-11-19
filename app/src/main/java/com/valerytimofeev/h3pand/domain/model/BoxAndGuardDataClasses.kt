package com.valerytimofeev.h3pand.domain.model

import com.valerytimofeev.h3pand.data.local.additional_data.TextWithLocalization
import com.valerytimofeev.h3pand.data.local.database.BoxValueItem


/**
 * Box from database and guard number of this box.
 */
data class BoxWithGuard(
    val box: BoxValueItem,
    val guardNumber: GuardNumber
)

/**
 * Min, average and max number guard
 */
data class GuardNumber(
    val minGuard: Int,
    val avgGuard: Int,
    val maxGuard: Int,
    )

/**
 * Data class for GetBoxWithPercentUseCase output.
 */
data class BoxWithDropChance(
    val name: TextWithLocalization,
    var dropChance: Double,
    val mostLikelyGuard: Int,
    val range: IntRange,
    val type: String,
    val img: String,
)
