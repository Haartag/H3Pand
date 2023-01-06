package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.data.local.additional_data.MapSettings
import com.valerytimofeev.h3pand.data.local.database.AdditionalValueItem
import com.valerytimofeev.h3pand.data.local.database.BoxValueItem
import com.valerytimofeev.h3pand.data.local.database.Dwelling
import com.valerytimofeev.h3pand.data.local.additional_data.ItemWithFrequency
import com.valerytimofeev.h3pand.repositories.local.PandRepository
import com.valerytimofeev.h3pand.utils.Constants
import com.valerytimofeev.h3pand.utils.Resource
import com.valerytimofeev.h3pand.utils.Status
import javax.inject.Inject
import kotlin.math.roundToInt

class GetUnitDropCoefficientUseCase @Inject constructor(
    private val repository: PandRepository,
) {
    suspend operator fun invoke(
        boxValueItem: BoxValueItem,
        castle: Int,
        numberOfZones: Float,
        numberOfUnitZones: Float,
        mapSettings: MapSettings,
        zone: Int,
        exactlyGuard: Boolean
    ): Resource<Float> {

        if (zone !in mapSettings.valueRanges.indices) {
            return Resource.error("Error: Wrong map settings", null)
        }
        if (numberOfUnitZones > numberOfZones) {
            return Resource.error("An unknown error occurred", null)
        }

       if (mapSettings.flatCalculation) return Resource.success(0.1f)

        val itemsResource = getItemsList(
            castle,
            numberOfZones,
            numberOfUnitZones
        )
        val items = itemsResource.data!!

        val chancePairs = getMapChance(
            mapSettings,
            boxValueItem,
            zone
        )
        if (chancePairs.isEmpty()) return Resource.error("Error: Wrong map settings", null)
        var chanceSumm = 0.0f

        for (item in chancePairs) {
            var sumFrequence = 0.0f
            items.forEach {
                //0.25 to 1.0 of maximum value of current zone range
                if (it.value in (item.key.last * 0.25).roundToInt()..item.key.last) {
                    sumFrequence += it.frequency
                }
            }
            val chance = when (boxValueItem.type) {
                "Gold" -> if (exactlyGuard) {
                    mapSettings.goldRate.toFloat()
                } else {
                    (mapSettings.goldRate * item.value) / sumFrequence
                }
                "Exp" -> if (exactlyGuard) {
                    mapSettings.expRate.toFloat()
                } else {
                    (mapSettings.expRate * item.value) / sumFrequence
                }
                "Spell" -> if (exactlyGuard) {
                    mapSettings.spellRate.toFloat()
                } else {
                    (mapSettings.spellRate * item.value) / sumFrequence
                }
                else -> if (exactlyGuard) {
                    mapSettings.unitRate.toFloat()
                } else {
                    (mapSettings.unitRate * item.value) / sumFrequence
                }
            }
            chanceSumm += chance
        }
        return Resource.success(chanceSumm)
    }

    /**
     * Get list of map objects and they frequency from database [AdditionalValueItem], [Dwelling]
     * and from [ItemWithFrequency]
     */
    private suspend fun getItemsList(
        castle: Int,
        numberOfZones: Float,
        numberOfUnitZones: Float,

        ): Resource<List<ItemWithFrequency>> {
        val itemsWithFrequency = mutableListOf<ItemWithFrequency>()

        val addValuesResource = repository.getAdditionalValueWithFrequency()
        if (addValuesResource.status == Status.ERROR) return Resource.error(
            "Calculation error",
            null
        )
        val addValues = addValuesResource.data!!

        val dwellingsResource = repository.getDwellingsByCastle(castle)
        if (dwellingsResource.status == Status.ERROR) return Resource.error(
            "Calculation error",
            null
        )
        val dwellings = dwellingsResource.data!!

        val specialItems = ItemWithFrequency.specialItemsWithFrequency

        //Seer`s hut unit reward and other boxes can be added to increase precision

        itemsWithFrequency.addAll(addValues.map { it.toItemWithFrequency() })
        itemsWithFrequency.addAll(dwellings.map {
            it.toItemWithFrequency(
                numberOfZones, numberOfUnitZones
            ).data ?: return Resource.error("Calculation error", null)
        })
        itemsWithFrequency.addAll(specialItems)

        return Resource.success(itemsWithFrequency)
    }

    /**
     * Get box drop coefficient depends on map settings.
     */
    private fun getMapChance(
        mapSettings: MapSettings,
        boxValueItem: BoxValueItem,
        zone: Int
    ): Map<IntRange, Float> {
        val mapCoefficient = mutableMapOf<IntRange, Float>()

        val valueRanges = mapSettings.valueRanges[zone].valueRange
        for (item in valueRanges) {
            when {
                //if box value greater than current map value range - skip
                boxValueItem.value > item.range.last -> continue
                else -> {
                    //how often this value is picked in the selected range
                    val flatChance =
                        ((item.range.last - boxValueItem.value).toFloat() / item.range.last)
                    //frequency from map settings
                    val frequency = item.frequency
                    mapCoefficient[item.range] = flatChance * frequency
                }
            }
        }
        return mapCoefficient
    }

    /**
     * Convert additional value to value/frequence
     */
    private fun AdditionalValueItem.toItemWithFrequency() =
        ItemWithFrequency(this.value, this.frequency!!)

    /**
     * Convert dwelling to value/frequence
     */
    private fun Dwelling.toItemWithFrequency(
        numberOfZones: Float,
        numberOfUnitZones: Float,
        getDwellingValueUseCase: GetDwellingValueUseCase = GetDwellingValueUseCase()
    ): Resource<ItemWithFrequency> {
        val getDwellingValueResource = getDwellingValueUseCase(
            this,
            numberOfZones,
            numberOfUnitZones
        )
        if (getDwellingValueResource.status == Status.ERROR) {
            return Resource.error("An unknown error occurred", null)
        }
        return Resource.success(
            ItemWithFrequency(
                getDwellingValueResource.data!!,
                Constants.DWELLING_FREQUENCY
            )
        )
    }
}