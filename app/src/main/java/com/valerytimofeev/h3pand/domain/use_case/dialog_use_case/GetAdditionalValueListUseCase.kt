package com.valerytimofeev.h3pand.domain.use_case.dialog_use_case

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
    suspend operator fun invoke(): Resource<List<String>> {
        val addValueResource = repository.getAdditionalValueTypesList()
        if (addValueResource.status == Status.ERROR) {
            return Resource.error(addValueResource.message ?: "Database error occurred", null)
        }
        val addValueList = mutableListOf<String>()
        addValueList.add("Custom value")
        addValueList.addAll(addValueResource.data!!)
        addValueList.add("Dwelling")
        return Resource.success(addValueList)
    }
}