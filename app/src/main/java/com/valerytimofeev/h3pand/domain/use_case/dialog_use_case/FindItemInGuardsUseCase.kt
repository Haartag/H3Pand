package com.valerytimofeev.h3pand.domain.use_case.dialog_use_case

import com.valerytimofeev.h3pand.data.local.Guard
import com.valerytimofeev.h3pand.repositories.local.PandRepository
import com.valerytimofeev.h3pand.utils.Resource
import com.valerytimofeev.h3pand.utils.Status
import javax.inject.Inject

/**
 * Find string in [Guard] list
 */
class FindItemInGuardsUseCase @Inject constructor(
    private val repository: PandRepository,
){
    suspend operator fun invoke(
        input: String
    ): Resource<List<Guard>> {
        val guardResource = repository.getAllGuards()

        if (guardResource.status == Status.ERROR) {
            return Resource.error(
                guardResource.message ?: "An unknown search error occurred",
                null
            )
        }
        return Resource.success(
            guardResource.data!!.filter { it.name.lowercase().contains(input.lowercase()) }
        )
    }
}
