package com.valerytimofeev.h3pand.domain.use_case.dialog_use_case

import com.valerytimofeev.h3pand.domain.model.SearchItem
import com.valerytimofeev.h3pand.domain.use_case.GetDwellingsListUseCase
import com.valerytimofeev.h3pand.repositories.local.PandRepository
import com.valerytimofeev.h3pand.utils.Resource
import com.valerytimofeev.h3pand.domain.model.CurrentLocal
import com.valerytimofeev.h3pand.data.local.database.*
import javax.inject.Inject

/**
 * Find string in addValue name [AdditionalValueItem] and Dwelling name [Dwelling].
 */
class FindItemInAdditionalValuesUseCase @Inject constructor(
    private val repository: PandRepository,
    private val getDwellingsListUseCase: GetDwellingsListUseCase,
    private val convertToSearchListUseCase: ConvertToSearchListUseCase
) {
    suspend operator fun invoke(
        input: String,
        castle: Int,
    ): Resource<List<SearchItem>> {
        val localization = CurrentLocal.local
        val additionalValueResource = repository.getFullAdditionalValueList()
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