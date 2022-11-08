package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.data.additional_data.MapSettings
import com.valerytimofeev.h3pand.data.local.AdditionalValueItem
import com.valerytimofeev.h3pand.data.local.BoxValueItem
import com.valerytimofeev.h3pand.data.local.Dwelling
import com.valerytimofeev.h3pand.domain.model.ItemWithFrequence
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
        zone: Int
    ): Resource<Float> {

        if (zone !in mapSettings.valueRanges.indices) {
            return Resource.error("Error: Wrong map settings", null)
        }
        if (numberOfUnitZones > numberOfZones) {
            return Resource.error("An unknown error occurred", null)
        }

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
                "Gold" -> (mapSettings.goldRate *  item.value) / sumFrequence
                "Exp" -> (mapSettings.expRate *  item.value) / sumFrequence
                "Spell" -> (mapSettings.spellRate *  item.value) / sumFrequence
                else -> (mapSettings.unitRate *  item.value) / sumFrequence
            }
            chanceSumm += chance
        }
        return Resource.success(chanceSumm)
    }

    /**
     * Get list of map objects and they frequency from database [AdditionalValueItem], [Dwelling]
     * and from [ItemWithFrequence]
     */
    private suspend fun getItemsList(
        castle: Int,
        numberOfZones: Float,
        numberOfUnitZones: Float,

        ): Resource<List<ItemWithFrequence>> {
        val itemsWithFrequency = mutableListOf<ItemWithFrequence>()

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

        val specialItems = ItemWithFrequence.specialItemsWithFrequence

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
                    val frequency = item.frequence
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
        ItemWithFrequence(this.value, this.frequency!!)

    /**
     * Convert dwelling to value/frequence
     */
    private fun Dwelling.toItemWithFrequency(
        numberOfZones: Float,
        numberOfUnitZones: Float,
        getDwellingValueUseCase: GetDwellingValueUseCase = GetDwellingValueUseCase()
    ): Resource<ItemWithFrequence> {
        val getDwellingValueResource = getDwellingValueUseCase(
            this,
            numberOfZones,
            numberOfUnitZones
        )
        if (getDwellingValueResource.status == Status.ERROR) {
            return Resource.error("An unknown error occurred", null)
        }
        return Resource.success(
            ItemWithFrequence(
                getDwellingValueResource.data!!,
                Constants.DWELLING_FREQUENCE
            )
        )
    }
}