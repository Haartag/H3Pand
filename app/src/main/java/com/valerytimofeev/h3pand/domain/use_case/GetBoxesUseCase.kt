package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.data.local.Guard
import com.valerytimofeev.h3pand.utils.BoxWithDropPercent
import com.valerytimofeev.h3pand.utils.Constants.MAX_GENERATED_GUARD
import com.valerytimofeev.h3pand.utils.Constants.MIN_PAND_VALUE
import com.valerytimofeev.h3pand.utils.Difficult.Companion.getDifficultForZone
import com.valerytimofeev.h3pand.utils.GuardRanges
import com.valerytimofeev.h3pand.utils.MapSettings.Companion.getMapSettings
import com.valerytimofeev.h3pand.utils.Resource
import javax.inject.Inject
import kotlin.math.roundToInt

class GetBoxesUseCase @Inject constructor(
    private val getGuardCharacteristicsUseCase: GetGuardCharacteristicsUseCase,
    private val getBoxForGuardRangeUseCase: GetBoxForGuardRangeUseCase,
    private val getBoxWithPercentUseCase: GetBoxWithPercentUseCase,
) {

    suspend operator fun invoke(
        guardUnit: Guard,
        castle: Int,
        castleZones: Int,
        guardRangeIndex: Int,
        zoneType: Int,
        additionalValue: Int,
        week: Int,
        mapName: String
    ): Resource<List<BoxWithDropPercent>> {

        val mapSettings = getMapSettings(mapName)
            ?: return Resource.error("An unknown error occurred", null)

        val zoneLvl = mapSettings.valueRanges.getOrNull(zoneType)?.difficult
            ?: return Resource.error("An unknown error occurred", null)

        var valueRange = mapSettings.valueRanges.getOrNull(zoneType)?.valueRange
            ?: return Resource.error("An unknown error occurred", null)

        val zones = mapSettings.numberOfZones

        val difficult = getDifficultForZone(zoneLvl)
            ?: return Resource.error("An unknown error occurred", null)

        val chosenGuardRange = GuardRanges.range[guardRangeIndex]
            ?: return Resource.error("An unknown error occurred", null)

        val weekCorrectedMinGuardValue = chosenGuardRange.first.weekCorrection(week)
        val weekCorrectedMaxGuardValue = chosenGuardRange.last.weekCorrection(week)

        if (valueRange.first < MIN_PAND_VALUE) valueRange = MIN_PAND_VALUE..valueRange.last

        /**
         * Get guard range for input data
         */
        val guardRange = getGuardCharacteristicsUseCase(
            weekCorrectedMin = weekCorrectedMinGuardValue,
            weekCorrectedMax = weekCorrectedMaxGuardValue,
            minGuardOnMap = ((guardUnit.minOnMap + guardUnit.maxOnMap.toDouble()) / 2.0).toInt(),
            maxGuardOnMap = MAX_GENERATED_GUARD,
        )
        if (guardRange.data == null) return Resource.error(
            guardRange.message
                ?: "An unknown error occurred: error code 01GB", null
        )

        /**
         * Get all potentially valid boxes for guard range
         */
        val boxes = getBoxForGuardRangeUseCase(
            difficult = difficult,
            guardRange = guardRange.data,
            guardValue = guardUnit.AIValue,
            additionalValue = additionalValue,
            castleZones = castleZones,
            zones = zones,
            castle = castle
        )

        if (boxes.data == null) return Resource.error(
            boxes.message
                ?: "An unknown error occurred: error code 02GB", null
        )


        val boxesWithPercent = mutableListOf<BoxWithDropPercent>()

        /**
         * Take boxes that fit the restrictions of the zone and calculate drop chances.
         */
        for (index in boxes.data.indices) {
            if (boxes.data[index].value + additionalValue in valueRange) {
                boxesWithPercent.add(
                    getBoxWithPercentUseCase(
                        boxValueItem = boxes.data[index],
                        guardValue = guardRange.data,
                        difficult = difficult,
                        unitValue = guardUnit.AIValue,
                        week = week,
                        additionalValue = additionalValue,
                        chosenGuardRange = chosenGuardRange
                    ).data ?: continue
                )
            } else continue
        }

        val percentSum = boxesWithPercent.sumOf { it.dropChance }

        boxesWithPercent.forEach {
            it.dropChance = ((it.dropChance * 100 / percentSum) * 10).roundToInt() / 10.0
        }

        boxesWithPercent.sortByDescending { it.dropChance }

        return if (boxesWithPercent.isNotEmpty()) {
            Resource.success(boxesWithPercent)
        } else {
            Resource.error("An unknown error occurred: error code 11GB", null)
        }
    }

    /**
     * Guard is increased by 10% for each week after the first
     */
    private fun Int.weekCorrection(
        week: Int
    ): Double {
        var weekModifier = 1.0
        repeat(week - 1) {
            weekModifier *= 1.1
        }
        return (this / weekModifier)
    }
}