package com.valerytimofeev.h3pand.domain.model

import com.valerytimofeev.h3pand.data.additional_data.TextWithLocalization

/**
 * Data class for GetBoxWithPercentUseCase output.
 */
data class BoxWithDropPercent(
    val name: TextWithLocalization,
    var dropChance: Double,
    val mostLikelyGuard: Int,
    val range: IntRange,
    val type: String,
    val img: String,
)
