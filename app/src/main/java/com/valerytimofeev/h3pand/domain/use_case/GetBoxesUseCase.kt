package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.data.local.additional_data.Difficult.Companion.getDifficultForZone
import com.valerytimofeev.h3pand.data.local.additional_data.MapSettings.Companion.getMapSettings
import com.valerytimofeev.h3pand.data.local.database.Guard
import com.valerytimofeev.h3pand.domain.model.BoxWithDropChance
import com.valerytimofeev.h3pand.utils.Constants.MAX_GENERATED_GUARD
import com.valerytimofeev.h3pand.utils.Constants.MIN_PERCENT
import com.valerytimofeev.h3pand.utils.Resource
import javax.inject.Inject
import kotlin.math.roundToInt

/**
 * Get boxes for current input.
 */
class GetBoxesUseCase @Inject constructor(
    private val getGuardCharacteristicsUseCase: GetGuardCharacteristicsUseCase,
    private val getBoxWithPercentUseCase: GetBoxWithPercentUseCase,
    private val getZoneValueRangeUseCase: GetZoneValueRangeUseCase,
    private val getAllAvailableBoxesUseCase: GetAllAvailableBoxesUseCase,
    private val getValidBoxesAndGuardsUseCase: GetValidBoxesAndGuardsUseCase
) {
    suspend operator fun invoke(
        guardUnit: Guard,
        castle: Int,
        castleZones: Int,
        guardRange: IntRange,
        zoneType: Int,
        additionalValue: Int,
        week: Int,
        mapName: String
    ): Resource<List<BoxWithDropChance>> {

        val mapSettings = getMapSettings(mapName)

        val zoneLvl = mapSettings.valueRanges.getOrNull(zoneType)?.difficult
            ?: return Resource.error("An unknown error occurred", null)

        val valueRangesForZone = mapSettings.valueRanges.getOrNull(zoneType)?.valueRange
            ?: return Resource.error("An unknown error occurred", null)

        val valueRange = getZoneValueRangeUseCase(valueRangesForZone).data
            ?: return Resource.error("An unknown error occurred", null)

        val zones = mapSettings.numberOfZones

        val difficult = getDifficultForZone(zoneLvl)
            ?: return Resource.error("An unknown error occurred", null)

        val weekCorrectedMinGuardValue = guardRange.first.weekCorrection(week)
        val weekCorrectedMaxGuardValue = guardRange.last.weekCorrection(week)

        val guardCharacteristics = getGuardCharacteristicsUseCase(
            // Guard range for input data
            weekCorrectedMin = weekCorrectedMinGuardValue,
            weekCorrectedMax = weekCorrectedMaxGuardValue,
            minGuardOnMap = ((guardUnit.minOnMap + guardUnit.maxOnMap.toDouble()) / 2.0).toInt(),
            maxGuardOnMap = MAX_GENERATED_GUARD,
        )
        if (guardCharacteristics.data == null) return Resource.error(
            guardCharacteristics.message
                ?: "An unknown error occurred: error code 01GB", null
        )

        /**
         * List of all boxes, including units of the selected zone
         */
        val boxes = getAllAvailableBoxesUseCase(
            mapSettings = mapSettings,
            currentZone = zoneType,
            castleZones = castleZones,
            zones = zones,
            castle = castle
        )
        if (boxes.data == null) return Resource.error(
            boxes.message
                ?: "An unknown error occurred: error code 02GB", null
        )
        /**
         * All boxes with a calculated number of guards (without week correction, first week value)
         */
        val boxesWithGuards = getValidBoxesAndGuardsUseCase(
            boxes.data,
            guardUnit,
            additionalValue,
            difficult,
            valueRange
        )

        val boxesWithPercent = mutableListOf<BoxWithDropChance>()

        for (index in boxesWithGuards.indices) { //Take boxes that fit the restrictions of the zone and calculate drop chances.
            if (boxesWithGuards[index].box.value + additionalValue in valueRange) {
                boxesWithPercent.add(
                    getBoxWithPercentUseCase(
                        boxesWithGuards[index],
                        guardValue = guardCharacteristics.data,
                        week = week,
                        chosenGuardRange = guardRange,
                        castle = castle,
                        mapSettings = mapSettings,
                        numberOfZones = zones.toFloat(),
                        numberOfUnitZones = castleZones.toFloat(),
                        zoneType = zoneType,
                        exactlyGuard = guardRange.first == guardRange.last
                    ).data ?: continue
                )
            } else continue
        }

        val percentSum = boxesWithPercent.sumOf { it.dropChance }
        if (percentSum == 0.0) return Resource.error(
            "It is impossible to find boxes for this kind or amount of guard",
            null
        )

        boxesWithPercent.forEach {
            it.dropChance = ((it.dropChance * 100 / percentSum) * 10).roundToInt() / 10.0
        }
        boxesWithPercent.removeAll { it.dropChance < MIN_PERCENT }

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