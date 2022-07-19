package com.valerytimofeev.h3pand.repositories.local

import com.valerytimofeev.h3pand.data.local.AdditionalValueItem
import com.valerytimofeev.h3pand.data.local.BoxValueItem
import com.valerytimofeev.h3pand.data.local.GuardItem
import com.valerytimofeev.h3pand.utils.Resource


interface PandRepository {

    suspend fun getAllGuardsList(): Resource<List<GuardItem>>

    suspend fun getAllAdditionalValuesList(): Resource<List<AdditionalValueItem>>

    suspend fun getAllBoxesInRange(minValue: Int, maxValue: Int): Resource<List<BoxValueItem>>

}