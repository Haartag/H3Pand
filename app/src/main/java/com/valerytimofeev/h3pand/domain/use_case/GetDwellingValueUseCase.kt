package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.data.local.Dwelling
import com.valerytimofeev.h3pand.utils.SpecialDwellings
import kotlin.math.roundToInt

/**
 * Get value of dwelling based on unit characteristics and map settings
 */
class GetDwellingValueUseCase {
    operator fun invoke(
        dwelling: Dwelling,
        numberOfZones: Float,
        numberOfUnitZones: Float,
    ): Int {
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
            return (fullValue / 2.0f).roundToInt()
        }

        val unitAIValue = dwelling.AIValue
        val unitWeeklyGain = dwelling.weeklyGain
        var result = unitAIValue * (unitWeeklyGain * (1 + numberOfUnitZones / numberOfZones) +
                    (numberOfUnitZones / 2))
        //Tripple AIValue for some dwellings
        if (dwelling.dwellingName == "Estate" || dwelling.dwellingName == "Hovel") result = result.toInt() * 3.0f
        return result.roundToInt()
    }
}