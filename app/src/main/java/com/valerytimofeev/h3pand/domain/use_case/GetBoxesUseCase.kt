package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.data.local.Guard
import com.valerytimofeev.h3pand.domain.model.BoxWithDropPercent
import com.valerytimofeev.h3pand.utils.Constants.MAX_GENERATED_GUARD
import com.valerytimofeev.h3pand.utils.Constants.MIN_PAND_VALUE
import com.valerytimofeev.h3pand.domain.model.Difficult.Companion.getDifficultForZone
import com.valerytimofeev.h3pand.data.additional_data.GuardRanges
import com.valerytimofeev.h3pand.data.additional_data.MapSettings.Companion.getMapSettings
import com.valerytimofeev.h3pand.utils.Resource
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * Get boxes for current input.
 */
class GetBoxesUseCase @Inject constructor(
    private val getGuardCharacteristicsUseCase: GetGuardCharacteristicsUseCase,
    private val getBoxForGuardRangeUseCase: GetBoxForGuardRangeUseCase,
    private val getBoxWithPercentUseCase: GetBoxWithPercentUseCase,
    private val getZoneValueRangeUseCase: GetZoneValueRangeUseCase,
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

        val zoneLvl = mapSettings.valueRanges.getOrNull(zoneType)?.difficult
            ?: return Resource.error("An unknown error occurred", null)

        val valueRangesForZone = mapSettings.valueRanges.getOrNull(zoneType)?.valueRange
            ?: return Resource.error("An unknown error occurred", null)

        var valueRange = getZoneValueRangeUseCase(valueRangesForZone).data
            ?: return Resource.error("An unknown error occurred", null)

        val zones = mapSettings.numberOfZones

        val difficult = getDifficultForZone(zoneLvl)
            ?: return Resource.error("An unknown error occurred", null)

        val chosenGuardRange = GuardRanges.range[guardRangeIndex] //TODO make guard range IntRange, not index of IntRange
            ?: return Resource.error("An unknown error occurred", null)

        val weekCorrectedMinGuardValue = chosenGuardRange.first.weekCorrection(week)
        val weekCorrectedMaxGuardValue = chosenGuardRange.last.weekCorrection(week)

        if (valueRange.first < MIN_PAND_VALUE) valueRange = MIN_PAND_VALUE..valueRange.last

        val guardRange = getGuardCharacteristicsUseCase( // Guard range for input data
            weekCorrectedMin = weekCorrectedMinGuardValue,
            weekCorrectedMax = weekCorrectedMaxGuardValue,
            minGuardOnMap = ((guardUnit.minOnMap + guardUnit.maxOnMap.toDouble()) / 2.0).toInt(),
            maxGuardOnMap = MAX_GENERATED_GUARD,
        )
        if (guardRange.data == null) return Resource.error(
            guardRange.message
                ?: "An unknown error occurred: error code 01GB", null
        )

        val boxes = getBoxForGuardRangeUseCase( //All potentially valid boxes for guardRange
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

        for (index in boxes.data.indices) { //Take boxes that fit the restrictions of the zone and calculate drop chances.
            if (boxes.data[index].value + additionalValue in valueRange) {
                boxesWithPercent.add(
                    getBoxWithPercentUseCase(
                        boxValueItem = boxes.data[index],
                        guardValue = guardRange.data,
                        difficult = difficult,
                        unitValue = guardUnit.AIValue,
                        week = week,
                        additionalValue = additionalValue,
                        chosenGuardRange = chosenGuardRange,
                        castle = castle,
                        mapSettings = mapSettings,
                        numberOfZones = zones.toFloat(),
                        numberOfUnitZones = castleZones.toFloat(),
                        zoneType = zoneType
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
            Resource.error("It is impossible to find boxes for this kind or amount of guard", null)
        }
    }

    /**
     * Guard is increased by 10% for each week after the first.
     * This function revert it for calculations.
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