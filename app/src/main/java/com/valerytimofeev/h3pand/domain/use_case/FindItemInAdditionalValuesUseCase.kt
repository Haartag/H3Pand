package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.data.local.AdditionalValueItem
import com.valerytimofeev.h3pand.repositories.local.PandRepository
import com.valerytimofeev.h3pand.utils.Resource
import javax.inject.Inject

class FindItemInAdditionalValuesUseCase @Inject constructor(
    private val repository: PandRepository,
    private val getDwellingsListUseCase: GetDwellingsListUseCase
) {
    suspend operator fun invoke(
        input: String,
        castle: Int,
    ): Resource<List<String>> {
        val additionalTypesResource = repository.getAdditionalValueTypesList()
        if (additionalTypesResource.data == null) return Resource.error("Search error", null)

        val additionalTypesSet = additionalTypesResource.data.toSet()

        val additionalValueList = mutableListOf<AdditionalValueItem>()

        additionalTypesSet.forEach {
            additionalValueList.addAll(repository.getAdditionalValuesList(it).data
                ?: return Resource.error("Search error", null))
        }

        val dwellingsList = getDwellingsListUseCase(castle).data
            ?: return Resource.error("Search error", null)
        val resultList = mutableListOf<String>()

        resultList.addAll(additionalValueList.filter { it.name.lowercase().contains(input.lowercase()) }
            .map { it.name })
        resultList.addAll(dwellingsList.filter { it.dwellingName.lowercase().contains(input.lowercase()) }
            .map { it.dwellingName })

        return Resource.success(resultList)
    }
}