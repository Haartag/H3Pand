package com.valerytimofeev.h3pand.domain.use_case.dialog_use_case

import com.valerytimofeev.h3pand.data.local.additional_data.TextWithLocalization
import com.valerytimofeev.h3pand.repositories.local.PandRepository
import com.valerytimofeev.h3pand.utils.Resource
import com.valerytimofeev.h3pand.utils.Status
import javax.inject.Inject

/**
 * Get addValue categories from repository and add additional categories.
 */
class GetAdditionalValueListUseCase @Inject constructor(
    private val repository: PandRepository
) {
    suspend operator fun invoke(): Resource<List<TextWithLocalization>> {
        val addValueResource = repository.getAdditionalValueTypesList()
        if (addValueResource.status == Status.ERROR) {
            return Resource.error(addValueResource.message ?: "Database error occurred", null)
        }
        val addValueList = mutableListOf<TextWithLocalization>()
        addValueList.add(TextWithLocalization("Choose a value", "Выберите ценность"))
        addValueList.addAll(addValueResource.data!!)
        addValueList.add(TextWithLocalization("Dwellings", "Жилища"))
        return Resource.success(addValueList)
    }
}