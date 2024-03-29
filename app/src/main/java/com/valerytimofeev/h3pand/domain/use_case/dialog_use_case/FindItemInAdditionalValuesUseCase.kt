package com.valerytimofeev.h3pand.domain.use_case.dialog_use_case

import com.valerytimofeev.h3pand.data.local.additional_data.MapSettings
import com.valerytimofeev.h3pand.data.local.database.AdditionalValueItem
import com.valerytimofeev.h3pand.data.local.database.Dwelling
import com.valerytimofeev.h3pand.domain.model.CurrentLocal
import com.valerytimofeev.h3pand.domain.model.SearchItem
import com.valerytimofeev.h3pand.domain.use_case.GetDwellingsListUseCase
import com.valerytimofeev.h3pand.utils.Resource
import javax.inject.Inject

/**
 * Find string in addValue name [AdditionalValueItem] and Dwelling name [Dwelling].
 */
class FindItemInAdditionalValuesUseCase @Inject constructor(
    private val getDwellingsListUseCase: GetDwellingsListUseCase,
    private val convertToSearchListUseCase: ConvertToSearchListUseCase,
    private val getAdditionalValue: GetAdditionalValueUseCase
) {
    suspend operator fun invoke(
        input: String,
        castle: Int,
        map: MapSettings,
        zone: Int
    ): Resource<List<SearchItem>> {
        val localization = CurrentLocal.local
        val additionalValueResource = getAdditionalValue.getFullList(
            map = map,
            zone = zone
        )
        val dwellingsResource = getDwellingsListUseCase(castle)

        if (additionalValueResource.data.isNullOrEmpty()) {
            return Resource.error(
                additionalValueResource.message ?: "An unknown search error occurred",
                null
            )
        }
        if (dwellingsResource.data.isNullOrEmpty()) {
            return Resource.error(
                dwellingsResource.message ?: "An unknown search error occurred",
                null
            )
        }

        val additionalValueList = additionalValueResource.data
        val dwellingsList = dwellingsResource.data

        val searchList =  convertToSearchListUseCase(additionalValueList, dwellingsList)

        return when(localization) {
            0 -> Resource.success(searchList.filter {
                it.itemName.lowercase().contains(input.lowercase())
            })
            1 -> Resource.success(searchList.filter {
                it.itemNameRu.lowercase().contains(input.lowercase())
            })
            else -> Resource.error("Search error", null)
        }
    }
}