package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.data.local.database.Dwelling
import com.valerytimofeev.h3pand.domain.model.SearchItem
import com.valerytimofeev.h3pand.utils.Resource
import com.valerytimofeev.h3pand.utils.Status
import com.valerytimofeev.h3pand.domain.use_case.dialog_use_case.FindItemInAdditionalValuesUseCase
import javax.inject.Inject

/**
 * Get value of addValue obtained from a search.
 * @see [FindItemInAdditionalValuesUseCase]
 */
class GetValueForSearchItemUseCase @Inject constructor(
    private val getDwellingValueUseCase: GetDwellingValueUseCase
) {
    operator fun invoke(
        item: SearchItem,
        numberOfZones: Float,
        numberOfUnitZones: Float,
    ): Resource<Int> {
        return when {
            !item.isDwelling && item.addItemValue == null -> {
                Resource.error("An unknown error occurred", null)
            }
            !item.isDwelling -> Resource.success(item.addItemValue)
            item.unitValue == null || item.weeklyGain == null || item.castle == null -> {
                Resource.error("An unknown error occurred", null)
            }
            else -> {
                val dwelling = Dwelling(
                    item.itemName,
                    item.itemNameRu,
                    "",
                    "",
                    item.unitValue,
                    item.weeklyGain,
                    item.castle
                )
                val dwellingResource =
                    getDwellingValueUseCase(
                        dwelling,
                        numberOfZones,
                        numberOfUnitZones,
                    )
                if (dwellingResource.status == Status.ERROR) {
                    Resource.error(dwellingResource.message ?: "An unknown error occurred", null)
                } else {
                    Resource.success(dwellingResource.data!!)
                }
            }
        }
    }
}