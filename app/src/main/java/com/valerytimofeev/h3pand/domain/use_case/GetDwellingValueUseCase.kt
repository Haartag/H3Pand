package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.data.local.database.Dwelling
import com.valerytimofeev.h3pand.utils.Resource
import com.valerytimofeev.h3pand.data.local.additional_data.SpecialDwellings
import kotlin.math.roundToInt

/**
 * Get value of dwelling based on unit characteristics and map settings
 */
class GetDwellingValueUseCase {
    operator fun invoke(
        dwelling: Dwelling,
        numberOfZones: Float,
        numberOfUnitZones: Float,
    ): Resource<Int> {
        if (numberOfUnitZones > numberOfZones) return Resource.error("Error: wrong number of zones", null)
        //Dwelling value for multi-creature dwellings
        val specialDwellings = SpecialDwellings.values().map { it.dwellingName }
        if (specialDwellings.contains(dwelling.dwellingName)) {
            var fullValue = 0
            val unitsList = SpecialDwellings.values().first { it.dwellingName == dwelling.dwellingName }.units
            unitsList.forEach {
                val specialNumberOfUnitZones = if (it.unitCastle == 0) 1.0f else numberOfUnitZones
                fullValue += (it.unitValue * (it.unitWeeklyGain * (1 + specialNumberOfUnitZones / numberOfZones) +
                        (numberOfUnitZones / 2))).roundToInt()
            }
            return Resource.success((fullValue / 2.0f).roundToInt())
        }

        val unitAIValue = dwelling.AIValue
        val unitWeeklyGain = dwelling.weeklyGain
        var result = unitAIValue * (unitWeeklyGain * (1 + numberOfUnitZones / numberOfZones) +
                    (numberOfUnitZones / 2))
        //Tripple AIValue for some dwellings - maybe make object with them?
        if (dwelling.dwellingName == "Estate" || dwelling.dwellingName == "Hovel") result = result.toInt() * 3.0f
        return Resource.success(result.roundToInt())
    }
}