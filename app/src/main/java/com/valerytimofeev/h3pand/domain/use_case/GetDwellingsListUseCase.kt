package com.valerytimofeev.h3pand.domain.use_case

import com.valerytimofeev.h3pand.data.local.Dwelling
import com.valerytimofeev.h3pand.repositories.local.PandRepository
import com.valerytimofeev.h3pand.utils.Resource
import javax.inject.Inject

class GetDwellingsListUseCase @Inject constructor(
    private val repository: PandRepository
) {
    suspend operator fun invoke(castle: Int): Resource<List<Dwelling>> {
        return repository.getDwellingsByCastle(castle)
    }
}