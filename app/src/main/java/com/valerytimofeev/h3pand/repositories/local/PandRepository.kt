package com.valerytimofeev.h3pand.repositories.local

import com.valerytimofeev.h3pand.data.local.AdditionalValueItem
import com.valerytimofeev.h3pand.data.local.GuardItem


interface PandRepository {

    suspend fun getGuardByName(id: Int): GuardItem

    suspend fun getAdditionalValueByName(id: Int): AdditionalValueItem

}