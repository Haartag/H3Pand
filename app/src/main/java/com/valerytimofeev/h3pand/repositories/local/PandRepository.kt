package com.valerytimofeev.h3pand.repositories.local

import com.valerytimofeev.h3pand.data.local.AdditionalValueItem
import com.valerytimofeev.h3pand.data.local.GuardItem
import com.valerytimofeev.h3pand.utils.Resource


interface PandRepository {

    suspend fun getGuardById(id: Int): Resource<GuardItem>

    suspend fun getAdditionalValueById(id: Int): Resource<AdditionalValueItem>

}